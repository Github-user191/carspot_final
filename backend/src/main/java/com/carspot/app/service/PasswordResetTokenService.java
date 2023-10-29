package com.carspot.app.service;

import com.carspot.app.entity.PasswordResetToken;
import com.carspot.app.entity.User;

public interface PasswordResetTokenService {

    PasswordResetToken createPasswordResetToken(User user, String token);
    String validatePasswordResetToken(String token);
    User getUserByPasswordResetToken(String token);
}
