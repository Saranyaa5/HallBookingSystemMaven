package com.hallbooking.utils;

import javax.mail.*;
import javax.mail.internet.*;

import com.hallbooking.ConsoleColors;

import java.util.Properties;

public class EmailUtil {

    public static void sendEmail(String recipient, String subject, String body) {
        final String senderEmail = "gowrisankar25082003@gmail.com";
        final String senderPassword = "mbho ahoq lcke sktc";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

           
            MimeBodyPart htmlBodyPart = new MimeBodyPart();
            htmlBodyPart.setContent(body, "text/html"); 

            
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlBodyPart);

            
            message.setContent(multipart);

            Transport.send(message);
            System.out.println(ConsoleColors.GREEN+"Email sent successfully to " + recipient+" "+ConsoleColors.RESET);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}