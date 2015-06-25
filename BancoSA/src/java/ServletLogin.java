/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sun.corba.se.spi.protocol.RequestDispatcherDefault;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

/**
 *
 * @author Rita
 */
@WebServlet(urlPatterns = {"/Login"})
public class ServletLogin extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */

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

       /* RequestDispatcher rd = null;
        String usuario = request.getParameter("txtUsuario");
        String password = request.getParameter("txtPass");

        if (usuario.equals("rita") && password.equals("rita")) {
            rd = request.getRequestDispatcher("/menu.jsp");
        } else {
            rd = request.getRequestDispatcher("/index.jsp");
        }

        rd.forward(request, response);*/
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
        String usuario = request.getParameter("txtUsuario");
        String password = request.getParameter("txtPass");
        String btnIngresar = request.getParameter("btnIngresar");
        
         
        if (btnIngresar != null) {
            if (login(usuario, password)) {
                rd = request.getRequestDispatcher("/cambiarContraseña.jsp");
            } else {
                String result = "Haz olvidado tu usuario o contraseña?";
                request.setAttribute("result", result);
                rd = request.getRequestDispatcher("/index.jsp");
            }
        }
        
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

    private static Boolean login(java.lang.String usuario, java.lang.String password) {
        webservice.Datos_Service service = new webservice.Datos_Service();
        webservice.Datos port = service.getDatosPort();
        return port.login(usuario, password);
    }

}
