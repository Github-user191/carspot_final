package com.carspot.app.service.impl;

import com.carspot.app.entity.ConfirmationToken;
import com.carspot.app.entity.User;
import com.carspot.app.exception.exceptions.InvalidTokenException;
import com.carspot.app.repository.ConfirmationTokenRepository;
import com.carspot.app.repository.UserRepository;
import com.carspot.app.service.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.carspot.app.entity.ConfirmationToken.EXPIRATION_TIME_HOURS;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    @Override
    public ConfirmationToken createConfirmationToken(User user, String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user, token);
        // Save random UUID confirmation token to DB

        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken;
    }

    @Override
    public String validateConfirmationToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token);

        if(confirmationToken != null) {
            User user = confirmationToken.getUser();

            if(user.isEmailVerified()) {
//            confirmationTokenRepository.delete(confirmationToken);
                throw new InvalidTokenException("Account has already been verified! Please head to login");
            }

            if((confirmationToken.isConfirmationTokenExpired())) {
//                confirmationTokenRepository.delete(confirmationToken);
                throw new InvalidTokenException("Account confirmation link expired. Please click resend to receive a new confirmation link");
            }
            user.setEmailVerified(true);
            userRepository.save(user);
        } else {
            throw new InvalidTokenException("There was an error. Please check your email for verification");
        }

        return "Valid";
    }

    @Override
    public ConfirmationToken generateNewConfirmationToken(String expiredToken) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(expiredToken);


        if(confirmationToken != null) {
            User user = confirmationToken.getUser();

            if(user.isEmailVerified())
                throw new InvalidTokenException("Account has already been verified! Please head to login");

            confirmationToken.setToken(UUID.randomUUID().toString());
            confirmationToken.setCreatedAt(LocalDateTime.now());
            confirmationToken.setExpirationTime(LocalDateTime.now().plusHours(EXPIRATION_TIME_HOURS));
            confirmationTokenRepository.save(confirmationToken);
        } else {
            throw new InvalidTokenException("There was an error. Please check your email for verification");
        }

        return confirmationToken;
    }
}
