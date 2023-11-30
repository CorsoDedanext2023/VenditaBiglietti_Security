package it.dedagroup.security.service.imp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;
@Service
public class MailSenderService {

    @Value("smtp.office365.com")
    private String host;
    @Value("587")
    private int port;
    @Value("ticketwodedasender@outlook.it")
    private String username;
    @Value("ticketwo2023")
    private String password;
    @Value("true")
    private boolean auth;
    @Value("true")
    private boolean starttls;


    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", starttls);
        return mailSender;
    }

    public void inviaMail(String destinatario, String oggetto, String testo){
        SimpleMailMessage messaggio = new SimpleMailMessage();
        messaggio.setFrom("ticketwodedasender@outlook.it");
        messaggio.setTo(destinatario);
        messaggio.setSubject(oggetto);
        messaggio.setText(testo);
        JavaMailSender sender = javaMailSender();
        sender.send(messaggio);
    }
}
