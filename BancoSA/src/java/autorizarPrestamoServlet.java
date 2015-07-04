/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import control.Administracion;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Rita
 */
@WebServlet(urlPatterns = {"/autorizarPrestamo"})
public class autorizarPrestamoServlet extends HttpServlet {

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
        
        String resIDSPrestamos = getPrestamosAAprobar();
        ArrayList<String> listaIDSPrestamos  = admon.getLista(resIDSPrestamos, "<id>");
        
        String Imprimir = "<form name=\"frmAutorizar\" action=\"AutorizarServlet\" method=\"POST\">"
                 + "<table width=\"100%\"><tr><th>ID Prestamo</th><th>Autorizar</th></tr>";
        for(String s: listaIDSPrestamos){
            Imprimir += "<tr><td>"+s+"</td><td><input  type=\"submit\" value=\"Autorizar "+ s +"\" name=\"btnAutorizar\" id=\"btnAutorizar\"></td>"+"</tr>";
        }
        Imprimir += "</table></form>";
        Imprimir += "<form name=\"frmVer\" action=\"InfoPrestamo\" method=\"POST\">"
                 + "<table width=\"100%\"><tr><th>Ver Detalle</th></tr>";
        for(String s: listaIDSPrestamos){
            Imprimir += "<tr><td><input  type=\"submit\" value=\"Ver "+s+"\" name=\"btnVer\" id=\"btnVer\"></td>"+"</tr>";
        }
        Imprimir += "</table></form>";
        
        request.setAttribute("Imprimir", Imprimir);
         
        request.getRequestDispatcher("/mostrarConsultaResult.jsp").forward(request, response);
        
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

    private static String getPrestamosAAprobar() {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getPrestamosAAprobar();
    }

}
