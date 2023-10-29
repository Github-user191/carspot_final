package com.carspot.app.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;


@Service
public class ErrorValidationServiceImpl {

    public ResponseEntity<?> validationService(BindingResult result) {
        if(result.hasErrors()) {
            Map<Object, Object> errorMap = new HashMap<>();

            for(FieldError err: result.getFieldErrors()) {
                errorMap.put(err.getField(), err.getDefaultMessage());
            }
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
