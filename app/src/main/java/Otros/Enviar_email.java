package Otros;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class Enviar_email extends Thread {
    final String miCorreo = "qizitmonster@gmail.com";
    final String miContraseña = "issinatour90";
    final String servidorSMTP = "smtp.gmail.com";
    final String puertoEnvio = "465";
    String mailReceptor = null;
    String asunto = null;
    String mensaje=null;


    public Enviar_email(String mailReceptor, String asunto,String mensaje) {
        this.mailReceptor = mailReceptor;
        this.asunto = asunto;
        this.mensaje=mensaje;

    }

    public void run(){
        methodSendMail();
    }

    public void  methodSendMail(){
        Properties props = new Properties();
        props.put("mail.smtp.user", miCorreo);
        props.put("mail.smtp.host", servidorSMTP);
        props.put("mail.smtp.port", puertoEnvio);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", puertoEnvio);
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        SecurityManager security = System.getSecurityManager();

        try {
            Authenticator auth = new autentificadorSMTP();
            Session session = Session.getInstance(props, auth);
            // session.setDebug(true);

            MimeMessage msg = new MimeMessage(session);
            msg.setContent
                    (mensaje,
                            "text/html");
            System.out.println(msg.getContent());
            msg.setSubject(asunto);
            msg.setFrom(new InternetAddress(miCorreo));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
                    mailReceptor));
            Transport.send(msg);
        } catch (Exception mex) {
            mex.printStackTrace();
        }

    }

    private class autentificadorSMTP extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(miCorreo, miContraseña);
        }
    }

    /**
     * @param args
     */


}