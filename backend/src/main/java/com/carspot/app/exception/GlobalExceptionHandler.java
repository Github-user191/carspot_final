package com.carspot.app.exception;

import com.carspot.app.exception.exceptions.*;
import com.carspot.app.exception.response.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, WebRequest req) {
        EmailAlreadyExistsExceptionResponse exceptionResponse = new EmailAlreadyExistsExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handleEmailNotFoundException(EmailNotFoundException ex, WebRequest req) {
        EmailNotFoundExceptionResponse exceptionResponse = new EmailNotFoundExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handleBadRequestException(BadRequestException ex, WebRequest req) {
        BadRequestExceptionResponse exceptionResponse = new BadRequestExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handlePostNotFoundException(PostNotFoundException ex, WebRequest req) {
        PostNotFoundExceptionResponse exceptionResponse = new PostNotFoundExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handleAccountNotVerifiedException(AccountNotVerifiedException ex, WebRequest req) {
        AccountNotVerifiedExceptionResponse exceptionResponse = new AccountNotVerifiedExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handlePostAlreadyExistsException(PostAlreadyExistsException ex, WebRequest req) {
        PostAlreadyExistsExceptionResponse exceptionResponse = new PostAlreadyExistsExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handleFileUploadException(FileUploadException ex, WebRequest req) {
        FileUploadExceptionResponse exceptionResponse = new FileUploadExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handleInvalidTokenException(InvalidTokenException ex, WebRequest req) {
        InvalidTokenExceptionResponse exceptionResponse = new InvalidTokenExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handleReviewException(ReviewException ex, WebRequest req) {
        ReviewExceptionResponse exceptionResponse = new ReviewExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    public final ResponseEntity<?> handlePasswordException(PasswordException ex, WebRequest req) {
        PasswordExceptionResponse exceptionResponse = new PasswordExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handleAuthenticationException(AuthenticationException ex, WebRequest req) {
        AuthenticationExceptionResponse exceptionResponse = new AuthenticationExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
