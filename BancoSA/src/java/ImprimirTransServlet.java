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

/**
 *
 * @author Rita
 */
@WebServlet(urlPatterns = {"/ImprimirTrans"})
public class ImprimirTransServlet extends HttpServlet {

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
        String valorDDLCuenta = request.getParameter("btnImprimir");
        //String valor = request.getParameter("lblTrans");
        System.out.println(valorDDLCuenta);
       // System.out.println(valor);
        int pos = valorDDLCuenta.indexOf(" ");
        String idTrans = valorDDLCuenta.substring(pos + 1);
        
        System.out.println(idTrans);
        String infoTrans = getInfoTransaccion(Integer.parseInt(idTrans));
        String monto = admon.getCadenaEtiquetas(infoTrans, "<monto>");
        String idSalida = admon.getCadenaEtiquetas(infoTrans, "<idSalida>");
        String idDestino = admon.getCadenaEtiquetas(infoTrans, "<idDestino>");
        String seguro = admon.getCadenaEtiquetas(infoTrans, "<idSeguro>");
        String prestamo = admon.getCadenaEtiquetas(infoTrans, "<idPrestamo>");
        String tipo = admon.getCadenaEtiquetas(infoTrans, "<tipo>");
        String fecha = admon.getCadenaEtiquetas(infoTrans, "<fecha>");
        
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

    private static String getInfoTransaccion(int idTrans) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getInfoTransaccion(idTrans);
    }

}
