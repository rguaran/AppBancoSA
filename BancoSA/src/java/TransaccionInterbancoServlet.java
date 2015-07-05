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
@WebServlet(urlPatterns = {"/TransaccionInterbanco"})
public class TransaccionInterbancoServlet extends HttpServlet {

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
        String banco = session.getAttribute("banco").toString();
        if (banco.equals("bancoJava")) {

        }

        if (banco.equals("bancoJava")) {
            String respuesta;
            respuesta = getIdUsuario(user);
            String idUsuario = admon.getCadenaEtiquetas(respuesta, "<Id>");
            respuesta = getCuentasUsuario(Integer.parseInt(idUsuario));
            ArrayList<String> listacuentas = admon.getLista(respuesta, "<cuenta>");

            Usuario userr = new Usuario();
            userr.setNombreUsuario(user);

            Cuenta cuenta;
            for (String s : listacuentas) {
                cuenta = new Cuenta();
                cuenta.setIdCuenta(Integer.parseInt(s));
                userr.CrearCuenta(cuenta);
            }

            request.setAttribute("listaCuentas", userr.getCuentas());
        } else if (banco.equals("bancoASP")) {
            String retornoCuentas = retornoCuentas2(Integer.parseInt(user));
            String[] splitAmp = retornoCuentas.split("&");
            ArrayList<String> listaCuentas = new ArrayList<String>();
            for (String s : splitAmp) {
                String[] splitComas = s.split(",");
                listaCuentas.add(splitComas[0]);
            }
            request.setAttribute("listaCuentas", listaCuentas);
        } else {
            //Banco PHP//
            int[] listaCuentas = {};
            try { // This code block invokes the WebservicePort:iniciarSesion operation on web service
                PHP.Webservice webservice = new PHP.Webservice_Impl();
                PHP.WebservicePortType serviciosPHP = webservice.getWebservicePort();
                listaCuentas = serviciosPHP.listadoCuentas(Integer.parseInt(user));
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(PHP.Webservice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            request.setAttribute("listaCuentas", listaCuentas);
        }

        request.getRequestDispatcher("/realizarTInterbanco.jsp").forward(request, response);
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
        String banco = session.getAttribute("banco").toString();
        String monto, cuentaDestino, cuentaOrigen, bancoDestino;
        String cuentaEmisor = "";
        Administracion admon = new Administracion();
        bancoDestino = request.getParameter("listaBancos");
        cuentaDestino = request.getParameter("txtCuentaDestino");
        monto = request.getParameter("txtMonto");
        bancoDestino = request.getParameter("listaBancos");
        cuentaEmisor = request.getParameter("selectCuentas");
        double montoReal = Double.parseDouble(monto);
        if (banco.equals("bancoJava")) {
            int idCuentaEmisor = Integer.parseInt(cuentaEmisor);
            int idBanco = 0;
            // Miramos si consumimos web service de php o de asp //
            String respuesta = getIdUsuario(user);
            String idUsuario = admon.getCadenaEtiquetas(respuesta, "<Id>");
            if (bancoDestino.equals("bancoPHP")) {
                idBanco = getIdBanco("Banco PHP");
                if (getSaldo(Integer.parseInt(cuentaEmisor)) >= Double.parseDouble(monto)) {
                    // Realiza la transaccion //
                    try { // This code block invokes the WebservicePort:iniciarSesion operation on web service
                        PHP.Webservice webservice = new PHP.Webservice_Impl();
                        PHP.WebservicePortType serviciosPHP = webservice.getWebservicePort();

                        respuesta = serviciosPHP.abonar2(Integer.parseInt(cuentaDestino), Double.parseDouble(monto));
                        if (!respuesta.equals("ok")) {
                            respuesta = "<font color=\"red\">Cuenta inexistente</font>";
                            request.setAttribute("result", respuesta);
                        } else {
                            realizarTransferencia(idCuentaEmisor, montoReal, idBanco);
                            respuesta = "<font color=\"blue\">Saldo transferido exitosamente</font>";
                            request.setAttribute("result", respuesta);
                        }
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(PHP.Webservice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                } else {
                    // No realiza la transaccion //
                    respuesta = "<font color=\"red\">Saldo insuficiente para realizar esta transferencia</font>";
                    request.setAttribute("result", respuesta);
                }
            } else {
                // DESDE JAVA A BANCO ASP //
                idBanco = getIdBanco("Banco ASP");
                if (getSaldo(Integer.parseInt(cuentaEmisor)) >= Double.parseDouble(monto)) {
                    int cd = Integer.parseInt(cuentaDestino);
                    int mon = (int) montoReal;
                    //boolean res = transferencia(Integer.parseInt(cuentaEmisor), cd, mon, 500);
                    boolean res = abonar(cd, mon);
                    if (res == true) {
                        realizarTransferencia(idCuentaEmisor, montoReal, idBanco);
                        respuesta = "<font color=\"blue\">Saldo transferido exitosamente</font>";
                        request.setAttribute("result", respuesta);
                    } else {
                        // No realiza la transaccion //
                        respuesta = "<font color=\"red\">Error en transaccion, verificar datos.</font>";
                        request.setAttribute("result", respuesta);
                    }
                } else {
                    // No realiza la transaccion //
                    respuesta = "<font color=\"red\">Saldo insuficiente para realizar esta transferencia</font>";
                    request.setAttribute("result", respuesta);
                }

            }

        } else if (banco.equals("bancoASP")) {
            String respuesta;
            if (bancoDestino.equals("bancoPHP")) {
                // DE BANCO ASP A BANCO PHP //                
                try { // This code block invokes the WebservicePort:iniciarSesion operation on web service
                    PHP.Webservice webservice = new PHP.Webservice_Impl();
                    PHP.WebservicePortType serviciosPHP = webservice.getWebservicePort();
                    int idUser = Integer.parseInt(user);
                    respuesta = serviciosPHP.abonar2(Integer.parseInt(cuentaDestino), Double.parseDouble(monto));
                    if (!respuesta.equals("ok")) {
                        respuesta = "<font color=\"red\">Cuenta inexistente</font>";
                        request.setAttribute("result", respuesta);
                    } else {
                        retiro(Integer.parseInt(cuentaEmisor), (int) (Double.parseDouble(monto)));
                        respuesta = "<font color=\"blue\">Saldo transferido exitosamente</font>";
                        request.setAttribute("result", respuesta);
                    }
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(PHP.Webservice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            } else {
                // DE BANCO ASP A BANCO JAVA //
                String res = transferirBancosaASP(Integer.parseInt(cuentaDestino), Double.parseDouble(monto));
                if (res.equals("1")) {
                    respuesta = "<font color=\"blue\">Saldo transferido exitosamente</font>";
                    request.setAttribute("result", respuesta);
                    retiro(Integer.parseInt(cuentaEmisor), (int) (Double.parseDouble(monto)));
                } else {
                    respuesta = "<font color=\"red\">Cuenta inexistente</font>";
                    request.setAttribute("result", respuesta);
                }
            }
        } else {
            // Banco PHP //
            String respuesta;
            if (bancoDestino.equals("bancoJava")) {
                String res = transferirBancosaPHP(Integer.parseInt(cuentaDestino), Double.parseDouble(monto));
                if (res.equals("1")) {
                    respuesta = "<font color=\"blue\">Saldo transferido exitosamente</font>";
                    request.setAttribute("result", respuesta);
                    try { // This code block invokes the WebservicePort:iniciarSesion operation on web service
                        PHP.Webservice webservice = new PHP.Webservice_Impl();
                        PHP.WebservicePortType serviciosPHP = webservice.getWebservicePort();
                        serviciosPHP.retiro(Integer.parseInt(user), Integer.parseInt(cuentaEmisor), montoReal);
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(PHP.Webservice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                } else {
                    respuesta = "<font color=\"red\">Cuenta inexistente</font>";
                    request.setAttribute("result", respuesta);
                }
            } else {
                int cd = Integer.parseInt(cuentaDestino);
                int mon = (int) montoReal;
                boolean res = abonar(cd, mon);
                if (res == true) {
                    try { // This code block invokes the WebservicePort:iniciarSesion operation on web service
                        PHP.Webservice webservice = new PHP.Webservice_Impl();
                        PHP.WebservicePortType serviciosPHP = webservice.getWebservicePort();
                        serviciosPHP.retiro(Integer.parseInt(user), Integer.parseInt(cuentaEmisor), montoReal);
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(PHP.Webservice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                    respuesta = "<font color=\"blue\">Saldo transferido exitosamente</font>";
                    request.setAttribute("result", respuesta);
                } else {
                    // No realiza la transaccion //
                    respuesta = "<font color=\"red\">Error en transaccion, verificar datos.</font>";
                    request.setAttribute("result", respuesta);
                }
            }
        }
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

<<<<<<< HEAD
    private static String realizarTransferencia(int idCuentaEmisor, double monto, int idBanco) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.realizarTransferencia(idCuentaEmisor, monto, idBanco);
    }

    private static Double getSaldo(int idCuenta) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getSaldo(idCuenta);
    }

    private static int getIdBanco(java.lang.String banco) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.getIdBanco(banco);
    }

    private static boolean abonar(int destino, int monto) {
        clienteASP.WebService1 service = new clienteASP.WebService1();
        clienteASP.WebService1Soap port = service.getWebService1Soap();
        return port.abonar(destino, monto);
    }

    private static boolean abonar_1(int destino, int monto) {
        clienteASP.WebService1 service = new clienteASP.WebService1();
        clienteASP.WebService1Soap port = service.getWebService1Soap();
        return port.abonar(destino, monto);
    }

    private static boolean retiro(int cuenta, int monto) {
        clienteASP.WebService1 service = new clienteASP.WebService1();
        clienteASP.WebService1Soap port = service.getWebService1Soap();
        return port.retiro(cuenta, monto);
    }

    private static String retornoCuentas2(int idcliente) {
        clienteASP.WebService1 service = new clienteASP.WebService1();
        clienteASP.WebService1Soap port = service.getWebService1Soap();
        return port.retornoCuentas2(idcliente);
    }

    private static String transferirBancosaASP(int idCuentaDestino, double monto) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.transferirBancosaASP(idCuentaDestino, monto);
    }

    private static String transferirBancosaPHP(int idCuentaDestino, double monto) {
        WSclientes.Servicios_Service service = new WSclientes.Servicios_Service();
        WSclientes.Servicios port = service.getServiciosPort();
        return port.transferirBancosaPHP(idCuentaDestino, monto);
    }
=======
    
>>>>>>> origin/master

}
