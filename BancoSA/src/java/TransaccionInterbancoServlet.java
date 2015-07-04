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
import java.util.ArrayList;
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
@WebServlet(urlPatterns = {"/TransaccionInterbanco"})
public class TransaccionInterbancoServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

        } finally {
            out.close();
        }
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

        request.getRequestDispatcher("/realizarTInterbanco.jsp").forward(request, response);
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
        String monto, cuentaDestino, cuentaOrigen, bancoDestino;
        Administracion admon = new Administracion();
        if (banco.equals("bancoJava")) {
            cuentaDestino = request.getParameter("txtCuentaDestino");
            monto = request.getParameter("txtMonto");
            bancoDestino = request.getParameter("listaBancos");

            // Miramos si consumimos web service de php o de asp //
            if (bancoDestino.equals("bancoPHP")) {
                //String respuesta = getIdUsuario(user);
                //String idUsuario = admon.getCadenaEtiquetas(respuesta, "<Id>");
                
                try { // This code block invokes the WebservicePort:iniciarSesion operation on web service
                    PHP.Webservice webservice = new PHP.Webservice_Impl();
                    PHP.WebservicePortType serviciosPHP = webservice.getWebservicePort();
                    
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(PHP.Webservice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            } else {

            }

        } else if (banco.equals("bancoASP")) {

        } else {
            // Banco PHP //
            monto = request.getParameter("txtMonto");
            cuentaOrigen = request.getParameter("txtCuentaOrigen");
            cuentaDestino = request.getParameter("txtCuentaDestino");

            // Seleccionamos el banco destino //
        }

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

    

}
