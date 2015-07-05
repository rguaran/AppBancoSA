/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
 * @author Rita
 */
@WebServlet(urlPatterns = {"/TransaccionServlet"})
public class TransaccionServlet extends HttpServlet {

    Administracion admon = new Administracion();

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
        } else if (banco.equals("bancoASP")) {
            String retornoCuentas = retornoCuentas2(Integer.parseInt(user));
            String[] splitAmp = retornoCuentas.split("&");
            ArrayList<String> listaCuentas = new ArrayList<String>();
            for (String s : splitAmp) {
                String[] splitComas = s.split(",");
                listaCuentas.add(splitComas[0]);
            }
            request.setAttribute("listaCuentas", listaCuentas);
        } else {
            //Banco PHP//
            int[] listaCuentas = {};
            try { // This code block invokes the WebservicePort:iniciarSesion operation on web service
                PHP.Webservice webservice = new PHP.Webservice_Impl();
                PHP.WebservicePortType serviciosPHP = webservice.getWebservicePort();
                listaCuentas = serviciosPHP.listadoCuentas(Integer.parseInt(user));
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(PHP.Webservice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            request.setAttribute("listaCuentas", listaCuentas);
        }

        request.getRequestDispatcher("/realizarTransaccion.jsp").forward(request, response);
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
        String valorDDLCuenta = request.getParameter("selectCuentas");
        String cantidad = request.getParameter("txtMonto");
        String cuentaDestino = request.getParameter("txtCuentaDestino");

        Double valDouble = Double.parseDouble(cantidad);
        String respuesta = "";
        if (valDouble <= 0) {
            respuesta = "<font color=\"red\">El monto debe de ser mayor a cero</font>";
            request.setAttribute("result", respuesta);
        } else {
            if (Integer.parseInt(cuentaDestino) == Integer.parseInt(valorDDLCuenta)) {
                respuesta = "<font color=\"red\">La cuenta destino no puede ser igual que la cuenta origen</font>";
                request.setAttribute("result", respuesta);
            } else {

                if (banco.equals("bancoJava")) {
                    String residTipoTrans = getIdTipoTrans("Envio local");
                    String idTT = admon.getCadenaEtiquetas(residTipoTrans, "<id>");
                    String resTransferecia = transferirSaldoLocal(Integer.parseInt(valorDDLCuenta), Integer.parseInt(cuentaDestino), Double.parseDouble(cantidad), Integer.parseInt(idTT));
                    String bandera = admon.getCadenaEtiquetas(resTransferecia, "<bandera>");

                    if (bandera.equals("1")) { //monto>saldo
                        respuesta = "<font color=\"red\">El monto a pagar es mayor al saldo disponible</font>";
                        request.setAttribute("result", respuesta);
                    } else if (bandera.equals("2")) { // la cuenta destino no existe
                        respuesta = "<font color=\"red\">La cuenta destino no existe</font>";
                        request.setAttribute("result", respuesta);
                    } else if (bandera.equals("3")) { //exito en operacion
                        respuesta = "<font color=\"blue\">La operación se realizó con éxito";
                        String saldorestante = admon.getCadenaEtiquetas(resTransferecia, "<saldo>");
                        NumberFormat formatoSaldo = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
                        respuesta += ", su saldo restante es de " + formatoSaldo.format(Double.parseDouble(saldorestante)) + "</font>";
                        request.setAttribute("result", respuesta);
                    }
                } else if (banco.equals("bancoASP")) {
                    double saldo = saldoActual(Integer.parseInt(user), Integer.parseInt(valorDDLCuenta));
                    int montoReal = (int) (Double.parseDouble(cantidad));
                    int destino = Integer.parseInt(cuentaDestino);
                    if (saldo >= Double.parseDouble(cantidad)) {
                        boolean res = transferencia(Integer.parseInt(valorDDLCuenta), destino, montoReal);
                        if (res) {
                            respuesta = "<font color=\"blue\">Saldo transferido con exito</font>";
                            request.setAttribute("result", respuesta);
                        } else {
                            respuesta = "<font color=\"red\">Cuenta inexistente</font>";
                            request.setAttribute("result", respuesta);
                        }
                    } else {
                        respuesta = "<font color=\"red\">Saldo insuficiente</font>";
                        request.setAttribute("result", respuesta);
                    }
                } else {
                    // Banco PHP //
                    try { // This code block invokes the WebservicePort:iniciarSesion operation on web service
                        PHP.Webservice webservice = new PHP.Webservice_Impl();
                        PHP.WebservicePortType serviciosPHP = webservice.getWebservicePort();
                        String monto = serviciosPHP.verificarCuentaMonto(Integer.parseInt(user), Integer.parseInt(valorDDLCuenta), Double.parseDouble(cantidad));
                        if (monto.equals("ok")) {
                            String res = serviciosPHP.transaccionCuentas(Integer.parseInt(user), Integer.parseInt(valorDDLCuenta), Integer.parseInt(cuentaDestino), Double.parseDouble(cantidad));
                            if (res.equals("ok")) {
                                respuesta = "<font color=\"blue\">Saldo transferido exitosamente</font>";
                            } else {
                                respuesta = "<font color=\"red\">Cuenta de destino inexistente</font>";
                            }
                            request.setAttribute("result", respuesta);
                        } else {
                            respuesta = "<font color=\"red\">Saldo insuficiente</font>";
                            request.setAttribute("result", respuesta);
                        }
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(PHP.Webservice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                }

            }
        }

        request.getRequestDispatcher("/menuTransaccion.jsp").forward(request, response);
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

    private static String transferirSaldoLocal(int idCuenta, int idDestino, double monto, int idTT) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.transferirSaldoLocal(idCuenta, idDestino, monto, idTT);
    }

    private static String getIdTipoTrans(java.lang.String tipo) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getIdTipoTrans(tipo);
    }

    private static boolean transferencia(int cuenta1, int cuenta2, int monto) {
        clienteASP.WebService1 service = new clienteASP.WebService1();
        clienteASP.WebService1Soap port = service.getWebService1Soap();
        return port.transferencia(cuenta1, cuenta2, monto);
    }

    private static int saldoActual(int idcliente, int cuenta) {
        clienteASP.WebService1 service = new clienteASP.WebService1();
        clienteASP.WebService1Soap port = service.getWebService1Soap();
        return port.saldoActual(idcliente, cuenta);
    }

    private static String retornoCuentas2(int idcliente) {
        clienteASP.WebService1 service = new clienteASP.WebService1();
        clienteASP.WebService1Soap port = service.getWebService1Soap();
        return port.retornoCuentas2(idcliente);
    }

}
