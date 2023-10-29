package com.carspot.app.service;

import org.springframework.stereotype.Service;

public interface EmailService {
    void sendConfirmationEmail(String email, String confirmationToken);
    void sendContactUsEmail(String fullName, String contactUsEmailAddress, String contactUsMessage, String subject);
    void sendResetPasswordEmail(String email, String token);
}
