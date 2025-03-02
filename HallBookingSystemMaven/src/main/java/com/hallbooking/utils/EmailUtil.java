package com.hallbooking.utils;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

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
            message.setText(body);

            Transport.send(message);
            System.out.println("✅ Email sent successfully to " + recipient);

        } catch (MessagingException e) {
            System.err.println("❌ Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        sendEmail("recipient@example.com", "Test Email", "This is a test email from the Hall Booking System.");
    }
}