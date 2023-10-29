package com.carspot.app.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountDetailsRequest {
    private long id;

    @NotBlank(message = "Required")
    private String fullName;

    @NotBlank(message = "Required")
    @Email
    @Column(unique = true)
    private String emailAddress;

    @NotBlank(message = "Required")
    @Size(min=11, message = "Invalid number")
    private String mobileNumber;

}
