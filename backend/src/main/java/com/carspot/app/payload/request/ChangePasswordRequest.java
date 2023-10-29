package com.carspot.app.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Required")
    private String oldPassword;

    @NotBlank(message = "Required")
    @Size(min=6,message = "Must be longer than 6 characters")
    private String newPassword;
    //private String emailAddress;
}
