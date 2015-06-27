/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import control.Administracion;
import control.Cuenta;
import control.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
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
@WebServlet(urlPatterns = {"/TransaccionServlet"})
public class TransaccionServlet extends HttpServlet {

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
        
        request.getRequestDispatcher("/realizarTransaccion.jsp").forward(request, response);
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
        String cantidad = request.getParameter("txtMonto");
        String cuentaDestino = request.getParameter("txtCuentaDestino");
        
        String residTipoTrans = getIdTipoTrans("Envio local");
        String idTT = admon.getCadenaEtiquetas(residTipoTrans, "<id>");
        
        String resTransferecia = transferirSaldoLocal(Integer.parseInt(valorDDLCuenta), Integer.parseInt(cuentaDestino), Double.parseDouble(cantidad), Integer.parseInt(idTT));
        
        String bandera = admon.getCadenaEtiquetas(resTransferecia, "<bandera>");
        String respuesta="";
        
        if (bandera.equals("1")){ //monto>saldo
            respuesta = "El monto a pagar es mayor al saldo disponible";
        } else if (bandera.equals("2")) { // la cuenta destino no existe
            respuesta = "La cuenta destino no existe"; 
        } else if (bandera.equals("3")){ //exito en operacion
            respuesta = "La operación se realizó con éxito";
            String saldorestante = admon.getCadenaEtiquetas(resTransferecia, "<saldo>");
            NumberFormat formatoSaldo = NumberFormat.getCurrencyInstance(new Locale("es","MX"));
            respuesta += ", su saldo restante es de " + formatoSaldo.format( Double.parseDouble(saldorestante) );
        }
        
        request.setAttribute("result", respuesta);
        
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

    private static String transferirSaldoLocal(int idCuenta, int idDestino, double monto, int idTT) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.transferirSaldoLocal(idCuenta, idDestino, monto, idTT);
    }

    private static String getIdTipoTrans(java.lang.String tipo) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getIdTipoTrans(tipo);
    }

}
