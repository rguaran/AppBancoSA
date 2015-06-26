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
 * @author Rita
 */
@WebServlet(urlPatterns = {"/CrearCuenta"})
public class CrearCuentaServlet extends HttpServlet {

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
        
        RequestDispatcher rd = null;
        String monto = request.getParameter("txtInicial");
        HttpSession session = request.getSession();
        String usuario = session.getAttribute("usuario").toString();
        if(Double.parseDouble(monto)<=0){
            // Error //
        }else{
            Administracion admon = new Administracion();
            String respuesta;
            respuesta = getIdUsuario(usuario);
            String idUsuario = admon.getCadenaEtiquetas(respuesta, "<Id>");
            respuesta = crearCuenta(Integer.parseInt(idUsuario));
            String idCuenta = admon.getCadenaEtiquetas(respuesta, "<idCuenta>");
            String resDep = depositoInicial(Integer.parseInt(idCuenta),Double.parseDouble(monto));
            
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
        }
        
        //processRequest(request, response);
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

    private static String crearCuenta(int crearCuenta) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.crearCuenta(crearCuenta);
    }

    private static String depositoInicial(int idCuenta, double monto) {
        WSclientes2.Servicios3_Service service = new WSclientes2.Servicios3_Service();
        WSclientes2.Servicios3 port = service.getServicios3Port();
        return port.depositoInicial(idCuenta, monto);
    }

}
