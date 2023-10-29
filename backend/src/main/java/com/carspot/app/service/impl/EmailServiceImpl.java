package com.carspot.app.service.impl;

import com.carspot.app.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;


    @Override
    @Async
    public void sendConfirmationEmail(String email, String appUrl) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Account Activation!");
        mailMessage.setText("To confirm your account, please click here : " + appUrl + " Note: This link will expire after 24 hours.");
        mailSender.send(mailMessage);



    }

    @Async
    @Override
    public void sendContactUsEmail(String fullName, String contactUsEmailAddress, String contactUsMessage, String subject) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("b777jpuprhvtre@gmail.com");
        mailMessage.setFrom(contactUsEmailAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(contactUsMessage);
        mailSender.send(mailMessage);
    }

    @Async
    @Override
    public void sendResetPasswordEmail(String email, String appUrl) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Reset password");
        mailMessage.setText("To reset your password, please click here : " + appUrl + " Note: This link will expire after 24 hours.");
        mailSender.send(mailMessage);
    }


}
