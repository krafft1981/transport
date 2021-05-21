package com.rental.transport.service;

import com.rental.transport.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

//    @Value("${mail.smtp.auth}")
//    private String mailSmtpAuth;
//
//    @Value("${mail.smtp.starttls.enable}")
//    private Boolean mailSmtpStarttlsEnable;
//
//    @Value("${mail.smtp.host}")
//    private String mailSmtpHost;
//
//    @Value("${mail.smtp.port}")
//    private Integer mailSmtpPort;
//
//    @Value("${mail.smtp.ssl.trust}")
//    private String mailSmtpSslTrust;

    void sendVerifyEmail(CustomerEntity entity) {

    }
}
