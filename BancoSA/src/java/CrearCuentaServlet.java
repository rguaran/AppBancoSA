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
        
        String banco = session.getAttribute("banco").toString();
        String usuario = session.getAttribute("usuario").toString();
        
        if(banco.equals("bancoJava")){
        if(Double.parseDouble(monto)<=0){
            // Error //
            request.setAttribute("result", "Error: El monto debe de ser mayor o igual a $500.00");
            request.getRequestDispatcher("/menuCuenta.jsp").forward(request, response);
        }else{
            Administracion admon = new Administracion();
            String respuesta;
            respuesta = getIdUsuario(usuario);
            String idUsuario = admon.getCadenaEtiquetas(respuesta, "<Id>");
            respuesta = crearCuenta(Integer.parseInt(idUsuario));
            String idCuenta = admon.getCadenaEtiquetas(respuesta, "<idCuenta>");
            String resDep = depositoInicial(Integer.parseInt(idCuenta),Double.parseDouble(monto));
            
            request.setAttribute("result", "Cuenta creada! cuenta: " + idCuenta);
            request.getRequestDispatcher("/menuCuenta.jsp").forward(request, response);
        }
        }else if (banco.equals("bancoASP")){ //Banco ASP
            String cuenta = request.getParameter("txtCuenta");
            boolean resCuenta = agregarCuenta(Integer.parseInt(usuario), Integer.parseInt(monto), Integer.parseInt(cuenta));
            
            if (resCuenta){
                request.setAttribute("result", "Cuenta creada!");
                request.getRequestDispatcher("/menuCuenta.jsp").forward(request, response);
            }else {
                request.setAttribute("result", "Error al crear cuenta");
                request.getRequestDispatcher("/menuCuenta.jsp").forward(request, response);
            }
            
        }else if (banco.equals("bancoPHP")){
            String res="";
            try { // This code block invokes the WebservicePort:iniciarSesion operation on web service
                PHP.Webservice webservice = new PHP.Webservice_Impl();
                PHP.WebservicePortType _serviciosPHP = webservice.getWebservicePort();
                res = _serviciosPHP.aperturaCuenta(Integer.parseInt(usuario), Double.parseDouble(monto));
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(PHP.Webservice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            
            if (res.equals("ok")){
                request.setAttribute("result", "Cuenta creada: " + res);
                request.getRequestDispatcher("/menuCuenta.jsp").forward(request, response);
            }else {
                request.setAttribute("result", "Error al crear cuenta: " + res);
                request.getRequestDispatcher("/menuCuenta.jsp").forward(request, response);
            }
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
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.depositoInicial(idCuenta, monto);
    }

    private static boolean agregarCuenta(int idcliente, int monto, int cuenta) {
        clienteASP.WebService1 service = new clienteASP.WebService1();
        clienteASP.WebService1Soap port = service.getWebService1Soap12();
        return port.agregarCuenta(idcliente, monto, cuenta);
    }

   
   

}
