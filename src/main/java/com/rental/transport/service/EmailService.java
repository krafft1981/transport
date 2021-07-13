package com.rental.transport.service;

import com.rental.transport.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Service
public class EmailService {

    @Value("${mail.smtp.auth}")
    private String mailSmtpAuth;

    @Value("${mail.smtp.starttls.enable}")
    private Boolean mailSmtpStarttlsEnable;

    @Value("${mail.smtp.host}")
    private String mailSmtpHost;

    @Value("${mail.smtp.port}")
    private Integer mailSmtpPort;

    @Value("${mail.smtp.ssl.trust}")
    private String mailSmtpSslTrust;

    @Value("${mail.smtp.username}")
    private String mailSmtpUserName;

    @Value("${mail.smtp.password}")
    private String mailSmtpPassword;

    void sendVerifyEmail(CustomerEntity entity) throws MessagingException {
    }

//        Properties prop = new Properties();
//        prop.put("mail.smtp.auth", mailSmtpAuth);
//        prop.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
//        prop.put("mail.smtp.host", mailSmtpHost);
//        prop.put("mail.smtp.port", mailSmtpPort);
//        prop.put("mail.smtp.ssl.trust", mailSmtpSslTrust);
//        prop.put("mail.debug", "true");
//        prop.put("mail.smtp.timeout", "60000");
//
//        Message message = new MimeMessage(Session.getInstance(prop, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(mailSmtpUserName, mailSmtpPassword);
//            }
//        }));
//        message.setFrom(new InternetAddress("krafft1981@mail.ru"));
//        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(entity.getAccount()));
//        message.setSubject("Transport verify email");
//
//        String msg = "This is my first email using JavaMailer";
//
//        MimeBodyPart mimeBodyPart = new MimeBodyPart();
//        mimeBodyPart.setContent(msg, "text/html");
//
//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(mimeBodyPart);
//        message.setContent(multipart);
//
//        Transport.send(message);
//    }
}
