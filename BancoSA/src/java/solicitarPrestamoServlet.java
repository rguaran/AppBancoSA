/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import control.Administracion;
import control.Cuenta;
import control.TipoPrestamo;
import control.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
@WebServlet(urlPatterns = {"/solicitarPrestamo"})
public class solicitarPrestamoServlet extends HttpServlet {
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
        //RequestDispatcher rd = null;
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
        
        //*************************tipos de prestamos
        
        String listaidsTiposPrestamo = getIdsTipoPrestamo();
        ArrayList<String> listaTP = admon.getLista(listaidsTiposPrestamo, "<id>");
        String infoTT; 
        
        ArrayList<TipoPrestamo> TPs = new ArrayList<TipoPrestamo>();
        TipoPrestamo tp;
        for (String s : listaTP){
            infoTT = getInfoTipoPrestamo(Integer.parseInt(s));
            tp = new TipoPrestamo(Integer.parseInt(s), infoTT);
            TPs.add(tp);
        }
        
        request.setAttribute("listaTP", TPs);
        
        request.getRequestDispatcher("/solicitarPrestamo.jsp").forward(request, response);
        
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
        
        String valorDDLCuenta = request.getParameter("selectCuentas");
        String valorDDLTP = request.getParameter("selectTP");
        String cantidad = request.getParameter("txtMonto");
        
        String resTipo = getDesgloseTipoPrestamo(Integer.parseInt(valorDDLTP));
        int minimo = Integer.parseInt(admon.getCadenaEtiquetas(resTipo, "<minimo>"));
        int maximo = Integer.parseInt(admon.getCadenaEtiquetas(resTipo, "<maximo>"));
        int tasa = Integer.parseInt(admon.getCadenaEtiquetas(resTipo, "<tasa>"));
        double monto = Double.parseDouble(cantidad);
        
        String result="";
        if( !(minimo <= monto && monto <= maximo) ){
            result = "<font color=\"red\">La cantidad especificada no cumple con el rango de ese tipo de prestamo</font>";
            request.setAttribute("result", result);
        }else{
            String resSolicitar = solicitarPrestamo(Integer.parseInt(valorDDLCuenta), Double.parseDouble(cantidad),0, Integer.parseInt(valorDDLTP));
             result = "<font color=\"blue\">Prestamo solicitado, espera la aprobación. Prestamo "+resSolicitar+" </font>";
             request.setAttribute("result", result);
        }
        
        
        
        
        /*String idPrestamo = admon.getCadenaEtiquetas(resSolicitar, "");
        
        if (resSolicitar.equals("true")){
            result = "<font color=\"blue\">Prestamo solicitado, espera la aprobación</font>";
        }else if (resSolicitar.equals("false")){
            result = "<font color=\"red\">Prestamo No se pudo solicitar</font>";
        }*/
        
       
        
        request.getRequestDispatcher("/menuPrestamo.jsp").forward(request, response);
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

    private static String getIdsTipoPrestamo() {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getIdsTipoPrestamo();
    }

    private static String getInfoTipoPrestamo(int idTipoPrestamo) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getInfoTipoPrestamo(idTipoPrestamo);
    }

    private static String solicitarPrestamo(int idCuenta, double cantidad, int cuotas, int idTipoPrestamo) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.solicitarPrestamo(idCuenta, cantidad, cuotas, idTipoPrestamo);
    }

    private static String getDesgloseTipoPrestamo(int idTipoPrestamo) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getDesgloseTipoPrestamo(idTipoPrestamo);
    }

}
