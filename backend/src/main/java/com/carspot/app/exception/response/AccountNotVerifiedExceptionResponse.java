package com.carspot.app.exception.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountNotVerifiedExceptionResponse {
    private String accountNotVerified;
}
