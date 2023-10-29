package com.carspot.app.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "Required")
    @Email
    private String forgotPasswordEmail;
    }
