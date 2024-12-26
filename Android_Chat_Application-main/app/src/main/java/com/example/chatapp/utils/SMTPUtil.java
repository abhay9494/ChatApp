package com.example.chatapp.utils;

import com.example.chatapp.BuildConfig;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SMTPUtil {

    public static void sendEmail(String to, String subject, String body, String contentType) throws MessagingException {
        // SMTP configuration
        final String senderEmail = BuildConfig.SMTP_EMAIL;
        final String senderPassword = BuildConfig.SMTP_PASSWORD;

        // Properties for the SMTP server
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP server (Gmail in this case)
        properties.put("mail.smtp.port", "587"); // TLS port
        properties.put("mail.smtp.auth", "true"); // Enable authentication
        properties.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

        // Create a session with an authenticator
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
//            message.setText(body);
            message.setContent(body, contentType);

            // Send the email
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
