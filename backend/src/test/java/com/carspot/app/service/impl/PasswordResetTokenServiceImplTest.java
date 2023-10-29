package com.carspot.app.service.impl;

import com.carspot.app.entity.ConfirmationToken;
import com.carspot.app.entity.PasswordResetToken;
import com.carspot.app.entity.User;
import com.carspot.app.exception.exceptions.InvalidTokenException;
import com.carspot.app.repository.PasswordResetTokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetTokenServiceImplTest {

    @InjectMocks
    private PasswordResetTokenServiceImpl passwordResetTokenService;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    private PasswordResetToken passwordResetToken;
    private User user;

    @BeforeEach
    void setUp() {
        passwordResetTokenService = new PasswordResetTokenServiceImpl(passwordResetTokenRepository);
        user = new User(1, "admin", "admin@gmail.com", "+27339923823", "password");

        passwordResetToken = new PasswordResetToken(user, "6f7580a0-9e92-4265-9c6e-f5027e032423");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void canCreatePasswordResetToken() {
        PasswordResetToken token = passwordResetTokenService.createPasswordResetToken(user, passwordResetToken.getToken());

        assertThat(token.getToken()).isNotNull();
        assertEquals(passwordResetToken.getToken(), token.getToken());
        verify(passwordResetTokenRepository).save(token);

    }

    @Test
    void canValidatePasswordResetToken() {
        String token = passwordResetToken.getToken();

        when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(passwordResetToken);


        String result = passwordResetTokenService.validatePasswordResetToken(token);

        assertEquals("Valid", result);
    }

    @Test
    void willThrowIfPasswordResetTokenIsExpiredWhenValidatingPasswordResetToken() {
        String token = passwordResetToken.getToken();

        when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(passwordResetToken);

        passwordResetToken.setExpirationTime(LocalDateTime.MIN);
        assertThrows(InvalidTokenException.class, () -> passwordResetTokenService.validatePasswordResetToken(token),
                "Link has expired. Please generate a new link, you will be redirected shortly.");

        verify(passwordResetTokenRepository, atLeastOnce()).delete(passwordResetToken);
    }

    @Test
    void canGetUserByPasswordResetToken() {

        String token = passwordResetToken.getToken();
        when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(passwordResetToken);


        User foundUser = passwordResetTokenService.getUserByPasswordResetToken(token);

        assertEquals(user, foundUser);
    }


}