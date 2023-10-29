package com.carspot.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    private String fullName;
    private String emailAddress;
    private String jwt;
    private Collection<?> authorities;
}
