package com.carspot.app.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidLoginResponse {
    private String fullName;
    private String emailAddress;
    private String jwt;
    private Collection<?> authorities;

}
