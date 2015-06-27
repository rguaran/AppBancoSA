/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import control.Administracion;
import control.Cuenta;
import control.Transaccion;
import control.Usuario;
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
@WebServlet(urlPatterns = {"/consultarTransaccionesServlet"})
public class consultarTransaccionesServlet extends HttpServlet {

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
        
        request.getRequestDispatcher("/seleccionarCuentaTransacciones.jsp").forward(request, response);
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
        String valorDDLCuenta = request.getParameter("selectCuentas");
        
        String getTransacciones = getTransaccionesCuenta(Integer.parseInt(valorDDLCuenta));
        
         ArrayList<String> listaIdsTrans = admon.getLista(getTransacciones, "<id>");
         ArrayList<Transaccion> listaTrans = new ArrayList<Transaccion>();
         Transaccion transaccion;
         String infoTrans;
         String monto;
         String idSalida;
         String idDestino;
         String seguro;
         String prestamo;
         String tipo;
         String fecha;         
         
         for (String s: listaIdsTrans){
             infoTrans = getInfoTransaccion(Integer.parseInt(s));
             monto = admon.getCadenaEtiquetas(infoTrans, "<monto>");
             idSalida = admon.getCadenaEtiquetas(infoTrans, "<idSalida>");
             idDestino = admon.getCadenaEtiquetas(infoTrans, "<idDestino>");
             seguro = admon.getCadenaEtiquetas(infoTrans, "<idSeguro>");
             prestamo = admon.getCadenaEtiquetas(infoTrans, "<idPrestamo>");
             tipo = admon.getCadenaEtiquetas(infoTrans, "<tipo>");
             fecha = admon.getCadenaEtiquetas(infoTrans, "<fecha>");
             
             transaccion = new Transaccion(Integer.parseInt(s), Double.parseDouble(monto), Integer.parseInt(idSalida), Integer.parseInt(idDestino),Integer.parseInt(seguro),Integer.parseInt(prestamo),tipo,fecha);
             listaTrans.add(transaccion);
         }
         
         String Imprimir = "<form name=\"frmVerTransaccion\" action=\"ImprimirTrans\" method=\"POST\">"
                 + "<table width=\"100%\"><tr><th>ID Transaccion</th><th>Descripcion</th><th>Imprimir PDF</th></tr>";
         
         for (Transaccion t: listaTrans){
             Imprimir += "<tr><td>"+t.getIdTransaccion()+"</td>";
             if (t.getSeguro()== -1 && t.getPrestamo()== -1){ //es una transferencia
                 Imprimir += "<td>Cuenta origen: "+t.getIdSalida()+"; monto: "+t.getMonto()+"; Cuenta Destino: "+t.getIdDestino()+";"
                         + " fecha: "+t.getFecha()+"; tipo: "+t.getTipo()+"</td><td><input  type=\"submit\" value=\"Imprimir "+ t.getIdTransaccion() +"\" name=\"btnImprimir\" id=\"btnImprimir\"></td>";
             }else if (t.getSeguro()!= -1 && t.getPrestamo()== -1 && t.getIdDestino()==-1){ // es un seguro
                 Imprimir += "<td>Cuenta origen: "+t.getIdSalida()+"; monto: "+t.getMonto()+";"
                         + " fecha: "+t.getFecha()+"; tipo: "+t.getTipo()+"; Seguro: "+t.getSeguro()+"</td><td><input  type=\"submit\" value=\"Imprimir "+ t.getIdTransaccion() +"\" name=\"btnImprimir\" id=\"btnImprimir\"></td>";
             }else if (t.getSeguro()== -1 && t.getPrestamo()!= -1 && t.getIdDestino()==-1){ // es un prestamo
                 Imprimir += "<td>Cuenta origen: "+t.getIdSalida()+"; monto: "+t.getMonto()+";"
                         + " fecha: "+t.getFecha()+"; tipo: "+t.getTipo()+"; Prestamo: "+t.getPrestamo()+"</td><td><input  type=\"submit\" value=\"Imprimir "+ t.getIdTransaccion() +"\" name=\"btnImprimir\" id=\"btnImprimir\"></td>";
             }
             
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

    private static String getTransaccionesCuenta(int idCuenta) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getTransaccionesCuenta(idCuenta);
    }

    private static String getInfoTransaccion(int idTrans) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getInfoTransaccion(idTrans);
    }

}
