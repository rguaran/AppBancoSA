/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import control.Administracion;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(urlPatterns = {"/ConsultarInfoUsuario"})
public class ConsultarInfoUsuario extends HttpServlet {

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
        String infoUsuario = getInfoUsuario(Integer.valueOf(idUsuario));
        String dpi = admon.getCadenaEtiquetas(infoUsuario, "<dpi>");
        String nombre = admon.getCadenaEtiquetas(infoUsuario, "<nombre>");
        String apellido = admon.getCadenaEtiquetas(infoUsuario, "<apellido>");
        String telefono = admon.getCadenaEtiquetas(infoUsuario, "<telefono>");
        String dir = admon.getCadenaEtiquetas(infoUsuario, "<direccion>");
        String correo = admon.getCadenaEtiquetas(infoUsuario, "<correo>");
        String nombreUsuario = admon.getCadenaEtiquetas(infoUsuario, "<nombreusuario>");
        
        String Imprimir = "<center><h4>Id de Usuario "+idUsuario+"</h4></center><br><br>";
        Imprimir += "<table width=\"100%\"><tr><th></th><th></th></tr>"
                + "<tr><td>DPI:</td><td>" + dpi + 
                "</td></tr><tr><td>Nombres:</td><td>" + nombre +
                "</td></tr><tr><td>Apellidos:</td><td>" + apellido + 
                "</td></tr><tr><td>Telefono:</td><td>" + telefono + 
                "</td></tr><tr><td>Direccion:</td><td>"+dir+
                "</td></tr><tr><td>Correo:</td><td>"+correo+
                "</td></tr><tr><td>Nombre de Usuario:</td><td>"+nombreUsuario+
                "</td></tr></table>";

        request.setAttribute("Imprimir", Imprimir);

        request.getRequestDispatcher("mostrarConsultaResult.jsp").forward(request, response);
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

    private static String getInfoUsuario(int idUsuario) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getInfoUsuario(idUsuario);
    }

}
