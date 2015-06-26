/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import control.Cuenta;
import control.Usuario;
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

    public String getCadenaEtiquetas(String cadena, String etiqueta){
        int pos = cadena.indexOf(etiqueta);
        int lon = etiqueta.length();
        String cierre = "</"+etiqueta.substring(1, etiqueta.length());
        int fin = cadena.indexOf(cierre);
        return cadena.substring(pos + lon, fin);
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
        String telefono = request.getParameter("txtTelefono");
        String fechanac = request.getParameter("txtFechaNac");
       
        String respRegistro = registro(dpi, nombre, apellido, correo, direccion, telefono, fechanac);
        
        String etqbandera="<bandera>", etqusuario="<usuario>", etqpassword="<password>";
        int pos = respRegistro.indexOf(etqbandera);
        int lon = etqbandera.length();
        int fin;
        char resultado = respRegistro.charAt(pos + lon);

        
        String result="";
        String flag=""; 
        if( resultado == '2' ){
            result = "¡Correo ya registrado, revisa tu información!";
            request.setAttribute("mensaje", "<font color=\"red\" >"+result+"</font>");
        }else if( resultado == '3' ){
            result = "¡DPI ya registrado, revisa tu información!";
            request.setAttribute("mensaje", "<font color=\"red\" >"+result+"</font>");
        } else{
            String usuario = getCadenaEtiquetas(respRegistro, etqusuario);
            String password = getCadenaEtiquetas(respRegistro, etqpassword);
            result = "¡Exito! Recibiras un correo con información de ingreso";

            request.setAttribute("mensaje", "<font color=\"blue\" >"+result+"</font>");                     
        }
        
       
        request.setAttribute("mensaje", result);
        
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

    private static String registro(java.lang.String dpi, java.lang.String nombre, java.lang.String apellido, java.lang.String correo, java.lang.String direccion, java.lang.String telefono, java.lang.String fechaNac) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.registro(dpi, nombre, apellido, correo, direccion, telefono, fechaNac);
    }

    

  }
