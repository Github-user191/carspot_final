package com.carspot.app.service.impl;

import com.carspot.app.entity.PasswordResetToken;
import com.carspot.app.entity.User;
import com.carspot.app.exception.exceptions.InvalidTokenException;
import com.carspot.app.repository.PasswordResetTokenRepository;
import com.carspot.app.service.PasswordResetTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public PasswordResetToken createPasswordResetToken(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);
        return passwordResetToken;
    }



    @Override
    public String validatePasswordResetToken(String token) {

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if(passwordResetToken != null) {
            if(passwordResetToken.isPasswordResetTokenExpired()) {
                passwordResetTokenRepository.delete(passwordResetToken);
                throw new InvalidTokenException("Link has expired. Please generate a new link, you will be redirected shortly.");
            }

        } else {
            throw new InvalidTokenException("Link has expired. Please try again, you will be redirected shortly.");
        }

        return "Valid";
    }

    @Override
    public User getUserByPasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        return passwordResetToken.getUser();
    }
}
