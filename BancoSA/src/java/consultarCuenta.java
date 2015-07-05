/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import PHP.Datosusuario;
import PHP.Saldousuario;
import control.Administracion;
import control.Cuenta;
import control.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author MarioR
 */
@WebServlet(urlPatterns = {"/consultarCuenta"})
public class consultarCuenta extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String user = session.getAttribute("usuario").toString();
        String banco = session.getAttribute("banco").toString();

        if (banco.equals("bancoJava")) {
            String respuesta;
            respuesta = getIdUsuario(user);
            Administracion admon = new Administracion();
            String idUsuario = admon.getCadenaEtiquetas(respuesta, "<Id>");
            respuesta = getCuentasUsuario(Integer.parseInt(idUsuario));
            ArrayList<String> listacuentas = admon.getLista(respuesta, "<cuenta>");

            Usuario userr = new Usuario();
            userr.setNombreUsuario(user);

            Cuenta cuenta;
            for (String s : listacuentas) {
                cuenta = new Cuenta();
                cuenta.setIdCuenta(Integer.parseInt(s));
                userr.CrearCuenta(cuenta);
            }

            request.setAttribute("listaCuentas", userr.getCuentas());

            request.getRequestDispatcher("/consultarCuenta.jsp").forward(request, response);
        } else if (banco.equals("bancoPHP")) {
            int[] listaCuentas = {};
            try { // This code block invokes the WebservicePort:iniciarSesion operation on web service
                PHP.Webservice webservice = new PHP.Webservice_Impl();
                PHP.WebservicePortType _serviciosPHP = webservice.getWebservicePort();
                listaCuentas = _serviciosPHP.listadoCuentas(Integer.parseInt(user));
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(PHP.Webservice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

            request.setAttribute("listaCuentas", listaCuentas);
            request.getRequestDispatcher("/consultarCuenta.jsp").forward(request, response);

        } else { //Banco ASP
            String retornoCuentas = retornoCuentas2(Integer.parseInt(user));

            String[] splitAmp = retornoCuentas.split("&");
            ArrayList<String> listaCuentas = new ArrayList<String>();
            for (String s : splitAmp) {
                String[] splitComas = s.split(",");
                listaCuentas.add(splitComas[0]);
            }

            request.setAttribute("listaCuentas", listaCuentas);
            request.getRequestDispatcher("/consultarCuenta.jsp").forward(request, response);

        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String user = session.getAttribute("usuario").toString();
        String banco = session.getAttribute("banco").toString();

        if (banco.equals("bancoJava")) {
            Administracion admon = new Administracion();
            String idCuenta = request.getParameter("selectCuentas");
            String resultado = verInfoCuenta(Integer.parseInt(idCuenta));
            String saldo = admon.getCadenaEtiquetas(resultado, "<saldo>");
            String fechaCreacion = admon.getCadenaEtiquetas(resultado, "<fecha>");
            String numPres = admon.getCadenaEtiquetas(resultado, "<prestamos>");
            String numSeg = admon.getCadenaEtiquetas(resultado, "<seguros>");
            NumberFormat formatoSaldo = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

            String Imprimir = "<center><h4>Cuenta No. " + idCuenta + "</h4></center><br><br>";
            Imprimir += "<table width=\"100%\"><tr><th></th><th></th></tr>"
                    + "<tr><td>Fecha de creacion:</td><td>" + fechaCreacion + "</td></tr><tr><td>Saldo</td><td>" + formatoSaldo.format(Double.parseDouble(saldo)) + "</td></tr><tr><td>"
                    + "Total de prestamos realizados</td><td>" + numPres + "</td></tr><tr><td>Total de seguros realizados</td><td>" + numSeg + "</td></tr></table>";

            request.setAttribute("Imprimir", Imprimir);

            request.getRequestDispatcher("mostrarConsultaResult.jsp").forward(request, response);
        } else if (banco.equals("bancoPHP")) { //Banco PHP
            String idCuenta = request.getParameter("selectCuentas");

            try { // This code block invokes the WebservicePort:iniciarSesion operation on web service
                PHP.Webservice webservice = new PHP.Webservice_Impl();
                PHP.WebservicePortType _serviciosPHP = webservice.getWebservicePort();
                Saldousuario saldo = _serviciosPHP.consultarSaldo(Integer.parseInt(user), Integer.parseInt(idCuenta));
                NumberFormat formatoSaldo = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
                String Imprimir = "<center><h4>Cuenta No. " + idCuenta + "</h4></center><br><br>";
                Imprimir += "<table width=\"100%\"><tr><th></th><th></th></tr>"
                        + "<tr><td>Mensaje </td><td>" + saldo.getMsj_respuesta() + "</td></tr><tr><td>Saldo</td><td>" + formatoSaldo.format(saldo.getSaldo()) + "</td></tr><tr><td>"
                        + "Direccion</td><td>" + saldo.getDireccion() + "</td></tr></table>";

                request.setAttribute("Imprimir", Imprimir);

                request.getRequestDispatcher("mostrarConsultaResult.jsp").forward(request, response);

            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(PHP.Webservice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        } else { //Banco ASP
            String idCuenta = request.getParameter("selectCuentas");
            String retornoCuentas = retornoCuentas2(Integer.parseInt(user));

            String[] splitAmp = retornoCuentas.split("&");
            ArrayList<String> listaCuentas = new ArrayList<String>();
            ArrayList<String> listaSaldos = new ArrayList<String>();
            for (String s : splitAmp) {
                String[] splitComas = s.split(",");
                listaCuentas.add(splitComas[0]);
                listaSaldos.add(splitComas[1]);
            }

            int pos = listaCuentas.indexOf(idCuenta);
            Double saldo = Double.parseDouble(listaSaldos.get(pos));
            NumberFormat formatoSaldo = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
            String Imprimir = "<center><h4>Cuenta No. " + idCuenta + "</h4></center><br><br>";
            Imprimir += "<table width=\"100%\"><tr><th></th><th></th></tr>"
                    + "<tr><td>Saldo </td><td>" + formatoSaldo.format(saldo) + "</td></tr></table>";

            request.setAttribute("Imprimir", Imprimir);

            request.getRequestDispatcher("mostrarConsultaResult.jsp").forward(request, response);

        }
    }

    private static String verInfoCuenta(int cuenta) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.verInfoCuenta(cuenta);
    }

    private static String getIdUsuario(java.lang.String usuario) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getIdUsuario(usuario);
    }

    private static String getCuentasUsuario(int idUsuario) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getCuentasUsuario(idUsuario);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private static String retornoCuentas2(int idcliente) {
        clienteASP.WebService1 service = new clienteASP.WebService1();
        clienteASP.WebService1Soap port = service.getWebService1Soap12();
        return port.retornoCuentas2(idcliente);
    }

}
