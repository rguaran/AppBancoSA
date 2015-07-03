/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import org.apache.axis.client.Service; 
import org.apache.axis.client.Call;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode; 
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;



/**
 *
 * @author Rita
 */
public class PHPClient {

    public PHPClient() {
    }
    
    public String respuestaString(String interfaz, String [] nombreParametros, String []parametros){
        Service service = new Service();
        Call call;
        String[] params={};
        String respuesta = "";
        try {
            call = (Call) service.createCall();
            String ws_url = "http://10.42.0.1/phpserver/webservice.php";
            call.setTargetEndpointAddress(ws_url);
            call.clearOperation();
            call.setOperationName(new QName("http://10.42.0.1/phpserver/webservice.php?wsdl", interfaz));
            QName QNAME_TYPE_STRING = new QName("string");
            for( int i=0; i<nombreParametros.length; i++ ){
                String nombrePar = nombreParametros[i]; 
                call.addParameter(nombrePar, QNAME_TYPE_STRING, ParameterMode.IN);                
            }
            call.setReturnType(QNAME_TYPE_STRING);
            respuesta = (String) call.invoke(parametros);
            System.out.println(respuesta);
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return respuesta;
    }
    
    
    
    public String loginPHP(String usuario, String password){
    Service service = new Service();
    Call call;
    String respuesta="";
        try {
            call = (Call) service.createCall();
            String ws_url = "http://10.42.0.1//phpserver/webservice.php";
            call.setTargetEndpointAddress(ws_url);
            call.clearOperation();
            call.setOperationName(new QName("http://10.42.0.1/phpserver/webservice.php?wsdl", "iniciarSesion"));
            QName QNAME_TYPE_STRING = new QName("string");
            call.addParameter("usuario", QNAME_TYPE_STRING, ParameterMode.IN);
            call.addParameter("password", QNAME_TYPE_STRING, ParameterMode.IN);
            String[] params = {usuario, password};
            call.setReturnType(QNAME_TYPE_STRING);
            String response = (String) call.invoke(params);
            //System.out.println(response);
            respuesta = response;
        } catch (ServiceException e) {
            e.printStackTrace();
            respuesta = "Usuario o contraseña incorrectas";
        } catch (RemoteException e) {
            e.printStackTrace();
            respuesta = "Usuario o contraseña incorrectas";
        }
        return respuesta;
    }
    
     
    
    
}
