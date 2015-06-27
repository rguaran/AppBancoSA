/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Rita
 */
public class Correo {
    public Correo(){}
    
    public void EnviarCorreo(String receptor,String usuario, String contrasenia){
        //enviar correo
        Properties props = new Properties();
        
        // Nombre del host de correo, es smtp.gmail.com
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        
        // TLS si está disponible
        props.setProperty("mail.smtp.starttls.enable", "true");
        
        // Puerto de gmail para envio de correos
        props.setProperty("mail.smtp.port","587");
        
        // Nombre del usuario
        props.setProperty("mail.smtp.user", "bancosa.registro@gmail.com");
        
        // Si requiere o no usuario y password para conectarse.
        props.setProperty("mail.smtp.auth", "true");
        
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);
    
        MimeMessage message = new MimeMessage(session);
        try {
            // Quien envia el correo
            message.setFrom(new InternetAddress("bancosa.registro@gmail.com"));
        
        
        // A quien va dirigido
        //message.addRecipient(Message.RecipientType.TO, new InternetAddress("destinatario@dominio.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress( receptor ));
        
        message.setSubject( "Información de nuevo Usuario" );
        message.setText( "Hola. \n Es un gusto que seas parte de nuestro banco, tus credenciales "
                + "de usuario son: \n Usuario: "+ usuario + "\n Password: "+ contrasenia );
        
        /*
        message.setSubject("Hola");
        message.setText("Mensajito con Java Mail" +
        "de los buenos." +
        "poque si");
        
        message.setText(
        "Mensajito con Java Mail<br>" + "<b>de</b> los <i>buenos</i>." + "poque si",
        "ISO-8859-1",
        "html");
        */
        
        Transport t = session.getTransport("smtp");
        
        t.connect("bancosa.registro@gmail.com","administrador123");
        
        t.sendMessage(message,message.getAllRecipients());
        
        t.close();
        
        } catch (MessagingException ex) {
           // Logger.getLogger(EnviarCorreo.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
}
