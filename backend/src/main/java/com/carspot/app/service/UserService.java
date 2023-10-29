package com.carspot.app.service;

import com.carspot.app.dto.UserAndPostDTO;
import com.carspot.app.dto.UserLoginDTO;
import com.carspot.app.entity.ConfirmationToken;
import com.carspot.app.entity.User;
import com.carspot.app.payload.request.LoginRequest;
import com.carspot.app.payload.request.UpdateAccountDetailsRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    User findUserById(long id);
    User findUserByEmailAddress(String emailAddress);
    UserLoginDTO login(LoginRequest user);
    User register(User user);
    User updateUser(UpdateAccountDetailsRequest user, MultipartFile avatar) throws IOException;
    UserAndPostDTO getUserAndPostInfo(String emailAddress);

    void changePassword(String emailAddress, String oldPassword, String newPassword);
    void resetPassword(String emailAddress, String newPassword);
    boolean checkIfValidOldPassword(User user, String oldPassword);

}
