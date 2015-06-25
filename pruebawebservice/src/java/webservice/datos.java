/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Rita
 */
@WebService(serviceName = "datos")
public class datos {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "informacion")
    public String informacion(@WebParam(name = "nombre") String nombre, @WebParam(name = "carrera") String carrera, @WebParam(name = "matricula") int matricula, @WebParam(name = "anio") String anio) {
        //TODO write your implementation code here:
        String salida = "Podriamos decir que usted es " + nombre + ", esta en " + anio +
                              " anio, y su numero de matricula es " + matricula + " :) ";
        return salida;
    }
    
    @WebMethod(operationName = "login")
    public Boolean login(@WebParam(name = "usuario") String usuario, @WebParam(name="password") String password){
        if(usuario.equals("rita")&&password.equals("rita")){
            return true;
        }else{
            return false;
        }
    }
}
