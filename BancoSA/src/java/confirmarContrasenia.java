/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import control.Administracion;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
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
@WebServlet(urlPatterns = {"/confirmarContrasenia"})
public class confirmarContrasenia extends HttpServlet {

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
        processRequest(request, response);
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
        //processRequest(request, response);
        RequestDispatcher rd = null;
        String newPass = request.getParameter("txtNewPass");
        
        if(newPass.length()<6){
            request.setAttribute("result", "<font color=\"red\" >La contraseña debe ser de al menos 6 dígitos</font>");
            request.getRequestDispatcher("/cambiarContraseña.jsp").forward(request, response);
        }else{
            HttpSession session = request.getSession();
        
        String respuesta = confirmarCuenta((String)session.getAttribute("usuario"), newPass);
        
        Administracion admin = new Administracion();
        String bandera = admin.getCadenaEtiquetas(respuesta, "<Resultado>");
        
        if (bandera.equals("True")){
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
        }else{
            //error
        }
        }
        
        
        //rd = request.getRequestDispatcher("/crearUsuario.jsp");        
        
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

    private static String confirmarCuenta(java.lang.String usuario, java.lang.String password) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.confirmarCuenta(usuario, password);
    }

}
