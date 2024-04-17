package com.carspot.app.controller;

import com.carspot.app.event.RegistrationCompleteEvent;
import com.carspot.app.exception.exceptions.AccountNotVerifiedException;
import com.carspot.app.entity.ConfirmationToken;
import com.carspot.app.entity.User;
import com.carspot.app.payload.request.ChangePasswordRequest;
import com.carspot.app.payload.request.ForgotPasswordRequest;
import com.carspot.app.payload.request.LoginRequest;
import com.carspot.app.payload.response.ApiResponse;
import com.carspot.app.payload.response.ValidLoginResponse;
import com.carspot.app.security.MyUserDetails;
import com.carspot.app.security.jwt.JwtTokenProvider;
import com.carspot.app.service.*;
import com.carspot.app.service.impl.ErrorValidationServiceImpl;
import com.carspot.app.service.impl.UserDetailsServiceImpl;
import com.carspot.app.validation.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {


    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserValidator userValidator;
    private final ApplicationEventPublisher publisher;
    private final EmailService emailService;
    private final ErrorValidationServiceImpl errorValidationService;

    public AuthController(UserService userService, PasswordResetTokenService passwordResetTokenService, ConfirmationTokenService confirmationTokenService, ErrorValidationServiceImpl errorValidationService, UserDetailsServiceImpl userDetailsService, UserValidator userValidator, AuthenticationManager authenticationManager, ApplicationEventPublisher publisher, JwtTokenProvider tokenProvider, EmailService emailService) {
        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.confirmationTokenService = confirmationTokenService;
        this.errorValidationService = errorValidationService;
        this.userValidator = userValidator;
        this.publisher = publisher;
        this.emailService = emailService;
    }


    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest, BindingResult result, Principal principal, HttpServletRequest request) {

        ResponseEntity<?> errorMap = errorValidationService.validationService(result);

        if(errorMap != null) return errorMap;
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody User theUser, BindingResult result, final HttpServletRequest request) {
        // validation
        userValidator.validate(theUser, result);
        ResponseEntity<?> errorMap = errorValidationService.validationService(result);
        if(errorMap != null) return errorMap;

        userService.register(theUser);
        // publishes event which creates the user account + sends confirmation email
        publisher.publishEvent(new RegistrationCompleteEvent(theUser, applicationURL(request)));
        return ResponseEntity.ok(new ApiResponse("Registration success! Please check your email for a confirmation link", true));
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmToken(@RequestParam("token") String token) {
        String result = confirmationTokenService.validateConfirmationToken(token);

        User user = new User(1, "John Doe", "johndoe@gmail.com", "0772888222","password!");


        if(result.equalsIgnoreCase("valid")) {
            return ResponseEntity.ok(new ApiResponse("User Verified Successfully", true));
        }
        return ResponseEntity.ok(new ApiResponse("Invalid token", false));

    }

    @GetMapping("/resendConfirmationToken")
    public ResponseEntity<?> resendConfirmationToken(@RequestParam("token") String expiredToken, HttpServletRequest request) {
        ConfirmationToken confirmationToken = confirmationTokenService.generateNewConfirmationToken(expiredToken);
        System.out.println("in /resendConfirmationToken");
        User user = confirmationToken.getUser();

        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationURL(request)));
        return ResponseEntity.ok(new ApiResponse("Verification link sent, please check your email", true));

    }

    @PostMapping("/forgotPassword") // email address
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) {

        User user = userService.findUserByEmailAddress(forgotPasswordRequest.getForgotPasswordEmail());
        String url = "";

        if(user==null) {
            return ResponseEntity.badRequest().body("User with email " + user.getEmailAddress() + " does not exist");
        } else {
            String token = UUID.randomUUID().toString();
            passwordResetTokenService.createPasswordResetToken(user, token);
            url = createPasswordResetLink(applicationURL(request), token);
            System.out.println("url: " + url);
            emailService.sendResetPasswordEmail(forgotPasswordRequest.getForgotPasswordEmail(), url);
            return ResponseEntity.ok(new ApiResponse("Please check your email for a link to reset your password", true));
        }
    }

    @GetMapping("/confirmPasswordReset")
    public ResponseEntity<?> confirmPasswordResetToken(@RequestParam("token") String token) {
        String result = passwordResetTokenService.validatePasswordResetToken(token);

        if(result.equalsIgnoreCase("valid")) {
            return ResponseEntity.ok(new ApiResponse("Token is valid", true));
        }


        return ResponseEntity.ok(new ApiResponse("Invalid token", false));

    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token, @RequestBody ChangePasswordRequest changePasswordRequest) {
        String result = passwordResetTokenService.validatePasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid")) {
            return ResponseEntity.badRequest().body(new ApiResponse("Token is invalid" , false));
        }
        User user = passwordResetTokenService.getUserByPasswordResetToken(token);

        if(user != null) {
            userService.resetPassword(user.getEmailAddress(), changePasswordRequest.getNewPassword());
            return ResponseEntity.ok(new ApiResponse("Password reset successful. You will be redirected to login" , true));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse("TOKEN NULL" , false));
        }
    }


    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(Principal principal, @RequestBody ChangePasswordRequest changePasswordRequest ,BindingResult result) {
        ResponseEntity<?> errorMap = errorValidationService.validationService(result);

        if(errorMap != null) return errorMap;

        userService.changePassword(principal.getName(), changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
        return new ResponseEntity<>(new ApiResponse("Password changed successfully"), HttpStatus.OK);
    }


    private String createPasswordResetLink(String appUrl, String token) {
        String url = appUrl + "/reset-password/" + token;
        //log.info("Click the link below to reset your password: {}", url);

        return url;
    }


    private String applicationURL(HttpServletRequest request) {
        return "https://carspot.quest" + request.getContextPath();
    }



}
