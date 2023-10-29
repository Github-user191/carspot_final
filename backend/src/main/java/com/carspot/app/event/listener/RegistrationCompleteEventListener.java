package com.carspot.app.event.listener;


import com.carspot.app.event.RegistrationCompleteEvent;
import com.carspot.app.entity.User;
import com.carspot.app.service.ConfirmationTokenService;
import com.carspot.app.service.EmailService;
import com.carspot.app.service.impl.EmailServiceImpl;
import com.carspot.app.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {


    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;


    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        confirmationTokenService.createConfirmationToken(user, token);

        String url = "https://carspot.live/confirm-email/" + token;
        emailService.sendConfirmationEmail(user.getEmailAddress(), url);
        //log.info("Click the link to verify your account: {}", url);
    }
}
