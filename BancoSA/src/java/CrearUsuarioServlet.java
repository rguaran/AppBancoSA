/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Rita
 */
@WebServlet(urlPatterns = {"/CrearUsuario"})
public class CrearUsuarioServlet extends HttpServlet {

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
        RequestDispatcher rd = null;
        String dpi = request.getParameter("txtDPI");
        String nombre = request.getParameter("txtNombre");
        String apellido = request.getParameter("txtApellido");
        String correo = request.getParameter("txtCorreo");
        String direccion = request.getParameter("txtDireccion");
        int telefono = Integer.parseInt(request.getParameter("txtTelefono"));
        String fechanac = request.getParameter("txtFechaNac");
        
                
        String result = informacion(nombre, apellido, telefono, dpi);
        request.setAttribute("result", result);
        
        rd = request.getRequestDispatcher("/crearUsuario.jsp");
        
        rd.forward(request, response);
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

    private static String informacion(java.lang.String nombre, java.lang.String carrera, int matricula, java.lang.String anio) {
        webservice.Datos_Service service = new webservice.Datos_Service();
        webservice.Datos port = service.getDatosPort();
        return port.informacion(nombre, carrera, matricula, anio);
    }

  }
