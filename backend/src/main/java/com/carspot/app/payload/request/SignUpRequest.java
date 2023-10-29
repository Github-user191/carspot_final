package com.carspot.app.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    private String fullName;
    private String emailAddress;
    private String mobileNumber;
    private String password;
    private String confirmPassword;
}
