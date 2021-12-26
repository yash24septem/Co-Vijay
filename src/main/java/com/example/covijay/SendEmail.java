package com.example.covijay;


import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
    private String userEmail;
    private String otp;

    public SendEmail(String userEmail, String otp) {
        this.userEmail = userEmail;
        this.otp = otp;
    }

    public boolean sendMail() {
        // Enter the email address and password for the account from which verification link will be send
        String email = "medicare2019ee@gmail.com";
        String password = "babitaji";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject("Email Verification Link");
            message.setText("Your one time pin(otp) is: "+ otp);

            Transport.send(message);

            System.out.println("Successfully sent Verification Link");
            return true;
        } catch (Exception e) {
            System.out.println("Error at SendingEmail.java: " + e);
            return  false;
        }

    }

}
