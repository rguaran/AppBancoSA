/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import control.*;
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
@WebServlet(urlPatterns = {"/seleccionarCuenta"})
public class seleccionarCuentaServlet extends HttpServlet {

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
        for (String s : listacuentas ){
            cuenta = new Cuenta();
            cuenta.setIdCuenta(Integer.parseInt(s));
            userr.CrearCuenta(cuenta);
        }
        
        request.setAttribute("listaCuentas", userr.getCuentas());
        
        request.getRequestDispatcher("/seleccionarCuenta.jsp").forward(request, response);
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
        
        String valorDDLCuenta = request.getParameter("selectCuentas");
        String valorAconsultar = request.getParameter("txtConsultar");
        String valorDDLConsultar = request.getParameter("consultar");
        
        String Imprimir="";
        if (valorDDLConsultar.equals("Seguro")){
            String infoSeguro = getInfoSeguro(Integer.parseInt(valorDDLCuenta), Integer.parseInt(valorAconsultar));
            String bandera = admon.getCadenaEtiquetas(infoSeguro, "<bandera>");
            String total = admon.getCadenaEtiquetas(infoSeguro, "<total>");
            String cuotas= admon.getCadenaEtiquetas(infoSeguro, "<cuotas>");
            String prima= admon.getCadenaEtiquetas(infoSeguro, "<prima>");
            String fecha = admon.getCadenaEtiquetas(infoSeguro, "<fecha>");
            String tipo = admon.getCadenaEtiquetas(infoSeguro, "<tipo>");
            
            
        } else if (valorDDLConsultar.equals("Prestamo")) {
            String infoPrestamo = getInfoPrestamo(Integer.parseInt(valorDDLCuenta), Integer.parseInt(valorAconsultar));
            String bandera = admon.getCadenaEtiquetas(infoPrestamo, "<bandera>");
                        
            if (bandera.equals("2")){// correcto
                String cantidad = admon.getCadenaEtiquetas(infoPrestamo, "<cantidad>");
                String cuotas_pagadas = admon.getCadenaEtiquetas(infoPrestamo, "<cuotasPagadas>");
                String cuotas_pendientes = admon.getCadenaEtiquetas(infoPrestamo, "<cuotasPendientes>");
                String total_pagado = admon.getCadenaEtiquetas(infoPrestamo, "<totalPagado>");
                String total_pendiente = admon.getCadenaEtiquetas(infoPrestamo, "<totalPendiente>");
                String fecha = admon.getCadenaEtiquetas(infoPrestamo, "<fecha>");
            
                Imprimir += "<table width=\"100%\"><tr><th>Prestamo</th><th>Descripcion</th></tr>"
                    + "<tr><td>Cantidad</td><td>"+cantidad+"</td></tr><tr><td>Cuotas Pagadas</td><td>"+cuotas_pagadas+"</td></tr><tr><td>"
                    + "Cuotas pendientes</td><td>"+cuotas_pendientes+"</td></tr><tr><td>Total Pagado</td><td>"+total_pagado+"</td></tr><tr><td>"
                    + "Total pendiente</td><td>"+total_pendiente+"</td></tr><tr><td>fecha</td><td>"+fecha+"</td></tr></table>";
            }else if (bandera.equals("1")) { //no existe
                Imprimir += "No existe el préstamo";
            }
            
            request.setAttribute("Imprimir", Imprimir);
            
            request.getRequestDispatcher("mostrarConsultaResult.jsp").forward(request, response);
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

    private static String getInfoSeguro(int idCuenta, int idPrestamo) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getInfoSeguro(idCuenta, idPrestamo);
    }

    private static String getInfoPrestamo(int idCuenta, int idPrestamo) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getInfoPrestamo(idCuenta, idPrestamo);
    }

       

}
