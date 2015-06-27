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
        Administracion admon = new Administracion();
        String idCuenta = request.getParameter("selectCuentas");
        String resultado = verInfoCuenta(Integer.parseInt(idCuenta));
        String saldo = admon.getCadenaEtiquetas(resultado, "<saldo>");
        String fechaCreacion = admon.getCadenaEtiquetas(resultado, "<fecha>");
        String numPres = admon.getCadenaEtiquetas(resultado, "<prestamos>");
        String numSeg = admon.getCadenaEtiquetas(resultado, "<seguros>");
        NumberFormat formatoSaldo = NumberFormat.getCurrencyInstance(new Locale("es","MX"));

        String Imprimir = "<center><h4>Cuenta No. "+idCuenta+"</h4></center><br><br>";
        Imprimir += "<table width=\"100%\"><tr><th></th><th></th></tr>"
                + "<tr><td>Fecha de creacion:</td><td>" + fechaCreacion + "</td></tr><tr><td>Saldo</td><td>" + formatoSaldo.format( Double.parseDouble(saldo) ) + "</td></tr><tr><td>"
                + "Total de prestamos realizados</td><td>" + numPres + "</td></tr><tr><td>Total de seguros realizados</td><td>" + numSeg + "</td></tr></table>";

        request.setAttribute("Imprimir", Imprimir);

        request.getRequestDispatcher("mostrarConsultaResult.jsp").forward(request, response);
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

    

}
