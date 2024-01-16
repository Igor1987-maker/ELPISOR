package services;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service("MailService")
public class MailService extends GenericService {
	
	public static Session session;
	 String HOST = "10.1.0.246";
	
	public void sendMail(String from, String to, String subject, String msg) throws Exception {
       
        String USER = "omers@edusoftlearning.com";
        String PASSWORD = "Tamar2018$";
//        SimpleMailMessage message = new SimpleMailMessage();
        Properties props = new Properties();

        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.user", USER);

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.debug", "true");

        props.put("mail.smtp.socketFactory.port", "25");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");


        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(true);


        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setContent("<b>" + msg + "</b>", "text/html");
        message.setSubject(subject);
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress(to));
        Transport transport = session.getTransport("smtp");

        transport.connect(HOST, USER, PASSWORD);
      

        transport.sendMessage(message, message.getAllRecipients());

        transport.close();


    }
	
	
	
	private void createSession() throws Exception {

//        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Properties properties = new Properties();
//        properties.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
//        properties.setProperty("mail.pop3.socketFactory.fallback", "false");
//        properties.setProperty("mail.pop3.port", "995");
//        properties.setProperty("mail.pop3.socketFactory.port", "995");
//        properties.setProperty("mail.smtp.host", HOST);
//        properties.setProperty("mail.smtp.port", "465");
//        properties.setProperty("mail.smtp.auth", "true");
//        session = Session.getInstance(properties,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(Configuration.getInstance().getProperty("pop3username"), Configuration.getInstance().getProperty("pop3password"));
//                    }
//                });
		
		properties.put("mail.smtp.auth", "false");
	     //Put below to false, if no https is needed
	    properties.put("mail.smtp.starttls.enable", "true");
	    properties.put("mail.smtp.host", HOST);
	    properties.put("mail.smtp.port", 25);

	    session = Session.getInstance(properties);
    }

	

}
