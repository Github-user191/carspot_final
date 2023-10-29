package com.carspot.app.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountNotVerifiedException extends RuntimeException{
    public AccountNotVerifiedException(String message) {
        super(message);
    }
}
