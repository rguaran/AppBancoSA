/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import control.Administracion;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author MarioR
 */
@WebServlet(urlPatterns = {"/InfoPrestamo"})
public class InfoPrestamo extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet InfoPrestamo</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet InfoPrestamo at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        String valorboton = request.getParameter("btnVer");
        int pos = valorboton.indexOf(" ");
        String idPrestamo = valorboton.substring(pos + 1);
        Administracion admon = new Administracion();
        String respuesta = getInfoPrestamoCualquiera(Integer.parseInt(idPrestamo));
        
        String cantidad = admon.getCadenaEtiquetas(respuesta, "<cantidad>");
        String minimo = admon.getCadenaEtiquetas(respuesta, "<minimo>");
        String maximo = admon.getCadenaEtiquetas(respuesta, "<maximo>");
        String numCuotas = admon.getCadenaEtiquetas(respuesta, "<numCuotas>");
        String fecha = admon.getCadenaEtiquetas(respuesta, "<fecha>");
        String tasa = admon.getCadenaEtiquetas(respuesta, "<tasa>");
        String estado = admon.getCadenaEtiquetas(respuesta, "<estado>");
        String cpagadas = admon.getCadenaEtiquetas(respuesta, "<cpagadas>");
        String cpendientes = admon.getCadenaEtiquetas(respuesta, "<cpendientes>");
        String tpagado = admon.getCadenaEtiquetas(respuesta, "<tpagado>");
        String tpendiente = admon.getCadenaEtiquetas(respuesta, "<tpendiente>");
        String anios = String.valueOf( Double.parseDouble(numCuotas) / 12 );
        String valorCuota = String.valueOf( Double.parseDouble(cantidad) / Double.parseDouble( numCuotas ) + (Double.parseDouble(cantidad) / Double.parseDouble( numCuotas ))*Double.parseDouble(tasa)/100 );
        NumberFormat formatoSaldo = NumberFormat.getCurrencyInstance(new Locale("es","MX"));
        String Imprimir = "<center><h4>Prestamo No. "+idPrestamo+"</h4></center><br><br>";
        Imprimir += "<table width=\"100%\"><tr><th></th><th></th></tr>"
                + "<tr><td><b>Rango de prestamo:<b> Minimo: " + formatoSaldo.format( Double.parseDouble(minimo) ) + ". Maximo: " + formatoSaldo.format( Double.parseDouble(maximo) ) + "</td><td>"+
                "</td></tr><tr><td>Cantidad a prestar: " + formatoSaldo.format( Double.parseDouble(cantidad) ) + "</td><td>"+
                "</td></tr><tr><td>Pagando una cantidad de: " + numCuotas + " cuotas</td><td>"+
                "</td></tr><tr><td>Siendo un total de: " + anios + " años</td><td>"+
                "</td></tr><tr><td>A una tasa de: " + tasa + "%</td><td>"+
                "</td></tr><tr><td>Pagando el total de: " + formatoSaldo.format( Double.parseDouble(valorCuota) ) + " por cuota </td><td>"+
                "</td></tr><tr><td>Solicitado el: " + fecha + "</td><td>"+
                "</td></tr><tr><td>Teniendo un estado: " + estado + "</td><td>"+
                "</td></tr><tr><td>Cuotas pagadas: " + cpagadas + "</td><td>"+
                "</td></tr><tr><td>Cuotas pendientes: " + cpendientes + "</td><td>"+
                "</td></tr><tr><td>Total pagado: " + tpagado + "</td><td>"+
                "</td></tr><tr><td>Total pendiente: " + tpendiente + "</td><td>"+
                "</td></tr></table>";

        request.setAttribute("Imprimir", Imprimir);

        request.getRequestDispatcher("mostrarConsultaResult.jsp").forward(request, response);
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

    private static String getInfoPrestamoCualquiera(int idPrestamo) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getInfoPrestamoCualquiera(idPrestamo);
    }

    private static String datos(int idcliente) {
        clienteASP.WebService1 service = new clienteASP.WebService1();
        clienteASP.WebService1Soap port = service.getWebService1Soap();
        return port.datos(idcliente);
    }

}
