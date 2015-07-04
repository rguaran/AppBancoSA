/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import PHP.Saldousuario;
import com.sun.corba.se.spi.protocol.RequestDispatcherDefault;
import control.Cuenta;
import control.PHPClient;
import control.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import javax.xml.rpc.Call;
import javax.xml.rpc.ServiceException;
import javax.xml.ws.Service;

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
    public String getCadenaEtiquetas(String cadena, String etiqueta) {
        int pos = cadena.indexOf(etiqueta);
        int lon = etiqueta.length();
        String cierre = "</" + etiqueta.substring(1, etiqueta.length());
        int fin = cadena.indexOf(cierre);
        return cadena.substring(pos + lon, fin);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("Prueba de codigooo jaljlajoj fjlajsdi pirulin pin pon");
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

        RequestDispatcher rd = null;

        HttpSession session = request.getSession();
        session.setAttribute("usuario", null);
        //session.setAttribute("password", null);
        session.setAttribute("banco", null);
        //session.setAttribute("idCliente", null);
        rd = request.getRequestDispatcher("/index.jsp");

        rd.forward(request, response);
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

        String banco = request.getParameter("listaBancos");

        usuario = usuario.trim();

        if (banco.equals("bancoJava")) {
            String resp = login(usuario, password);
            String etqbandera = "<Bandera>", etqconfirmado = "<Confirmado>";
            String bandera = getCadenaEtiquetas(resp, etqbandera);

            if (bandera.equals("2")) {
                String result = "Usuario inexistente";
                request.setAttribute("result", result);
                rd = request.getRequestDispatcher("/index.jsp");
            } else if (bandera.equals("3")) {
                String result = "La contraseña es incorrecta. ¿Ya confirmaste tu cuenta?";
                request.setAttribute("result", result);
                rd = request.getRequestDispatcher("/index.jsp");
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                //session.setAttribute("password", password);
                session.setAttribute("banco", banco);

                String confirmado = getCadenaEtiquetas(resp, etqconfirmado);
                if (confirmado.equals("True")) {
                    rd = request.getRequestDispatcher("/menu.jsp");
                } else {
                    rd = request.getRequestDispatcher("/cambiarContraseña.jsp");
                }
            }
        } else if (banco.equals("bancoASP")) {
            boolean respuesta = login_1(usuario, password);

            if (respuesta) {
                HttpSession session = request.getSession();
                //session.setAttribute("usuario", usuario);
                //session.setAttribute("password", password);
                session.setAttribute("banco", banco);
                int idCliente = idclientes(usuario);
                session.setAttribute("usuario", idCliente);

                rd = request.getRequestDispatcher("/menu.jsp");
            } else {
                String result = "El usuario o contraseña es incorrecto";
                request.setAttribute("result", result);
                rd = request.getRequestDispatcher("/index.jsp");
            }
        } else if (banco.equals("bancoPHP")) {
            String res="";
            try { // This code block invokes the WebservicePort:iniciarSesion operation on web service
                PHP.Webservice webservice = new PHP.Webservice_Impl();
                PHP.WebservicePortType _serviciosPHP = webservice.getWebservicePort();
                res = _serviciosPHP.iniciarSesion(usuario, password);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(PHP.Webservice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            
             if (!res.equals("0")){
             HttpSession session = request.getSession();
             session.setAttribute("usuario", res);
             //session.setAttribute("password", password);
             session.setAttribute("banco", banco);
             rd = request.getRequestDispatcher("/menu.jsp");
             }else{
             String result = "El usuario o contraseña es incorrecto";
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

    private static String login(java.lang.String usuario, java.lang.String password) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.login(usuario, password);
    }

    private static boolean login_1(java.lang.String correo, java.lang.String contra) {
        clienteASP.WebService1 service = new clienteASP.WebService1();
        clienteASP.WebService1Soap port = service.getWebService1Soap12();
        return port.login(correo, contra);
    }

    private static int idclientes(java.lang.String correo) {
        clienteASP.WebService1 service = new clienteASP.WebService1();
        clienteASP.WebService1Soap port = service.getWebService1Soap12();
        return port.idclientes(correo);
    }

}
