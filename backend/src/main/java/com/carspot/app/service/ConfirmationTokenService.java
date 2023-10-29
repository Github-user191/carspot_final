package com.carspot.app.service;

import com.carspot.app.entity.ConfirmationToken;
import com.carspot.app.entity.User;


public interface ConfirmationTokenService {
    ConfirmationToken createConfirmationToken(User user, String token);
    String validateConfirmationToken(String token);
    ConfirmationToken generateNewConfirmationToken(String expiredToken);
}
