package com.carspot.app.service.impl;

import com.carspot.app.dto.UserAndPostDTO;
import com.carspot.app.dto.UserLoginDTO;
import com.carspot.app.exception.exceptions.*;
import com.carspot.app.entity.*;
import com.carspot.app.payload.request.LoginRequest;
import com.carspot.app.payload.request.UpdateAccountDetailsRequest;
import com.carspot.app.payload.response.ApiResponse;
import com.carspot.app.repository.*;
import com.carspot.app.security.MyUserDetails;
import com.carspot.app.security.jwt.JwtTokenProvider;
import com.carspot.app.service.FileUploadService;
import com.carspot.app.service.RoleService;
import com.carspot.app.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final ReviewRepository reviewRepository;
    private final FileUploadService fileUploadService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;


    @Override
    public User findUserById(long id) {
        User user = userRepository.getById(id);
        if (user == null) {
            throw new UsernameNotFoundException("User with id " + id + " does not exist");
        }
        return user;
    }

    @Override
    public User findUserByEmailAddress(String emailAddress) {
        User user = userRepository.findUserByEmailAddress(emailAddress);
        if (user == null) {
            throw new UsernameNotFoundException("User with email address " + emailAddress + " does not exist");
        }
        return user;
    }

    @Override
    public UserLoginDTO login(LoginRequest user) {

        UserLoginDTO userLoginDTO = null;
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmailAddress(), user.getPassword()));
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            String token = tokenProvider.createToken(authentication);

            userLoginDTO = new UserLoginDTO(userDetails.getFullName(), userDetails.getUsername(), token, userDetails.getAuthorities());
        } catch (DisabledException e) {
            throw new AuthenticationException("User account is disabled");
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid credentials");
        }
        return userLoginDTO;
    }

    @Override
    public User register(User user) {


        Role role = roleService.findByName(ERole.ROLE_USER);

        Set<Role> roleSet = new HashSet<>();
        user.setFullName(user.getFullName());
        user.setEmailAddress(user.getEmailAddress());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword("");
        roleSet.add(role);
        user.setRoles(roleSet);

        if (userRepository.existsByEmailAddress(user.getEmailAddress())) {
            throw new EmailAlreadyExistsException("Account already exists with this email");
        }
        return userRepository.save(user);
    }

    @Override
    public void changePassword(String emailAddress, String oldPassword, String newPassword) {
        User user = userRepository.findUserByEmailAddress(emailAddress);

        if (newPassword.length() < 6) {
            throw new PasswordException("Must be at least 6 characters");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new PasswordException("Old password is invalid");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


    @Override
    public void resetPassword(String emailAddress, String newPassword) {
        User user = userRepository.findUserByEmailAddress(emailAddress);

        if (newPassword.length() < 6) {
            throw new PasswordException("Must be at least 6 characters");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }


    @Override
    public User updateUser(UpdateAccountDetailsRequest user, MultipartFile avatar) throws IOException {
        User theUser = userRepository.getById(user.getId());

        List<Post> posts = theUser.getPosts();

        Review review = reviewRepository.findReviewByReviewerEmailAddress(user.getEmailAddress());

        theUser.setFullName(user.getFullName());
        theUser.setEmailAddress(user.getEmailAddress());
        theUser.setMobileNumber(user.getMobileNumber());

        // When a user changes their name/email, then change the values in the Post records of that user as well to maintain consistency
        for (Post post : posts) {
            post.setPostCreatorEmail(user.getEmailAddress());
            post.setPostCreatorName(user.getFullName());
        }

        if (review != null) {
            review.setReviewerFullName(user.getFullName());
            review.setReviewerEmailAddress(user.getEmailAddress());

            reviewRepository.save(review);
        }

        if (avatar != null) {
            fileUploadService.uploadUserAvatar(avatar, user.getEmailAddress());
        }
        userRepository.save(theUser);


        return theUser;
    }


    @Override
    public UserAndPostDTO getUserAndPostInfo(String emailAddress) {
        User user = userRepository.findUserByEmailAddress(emailAddress);

        if (user == null) {
            throw new UsernameNotFoundException("User with email address " + emailAddress + " does not exist");
        }


        return convertEntityToDTO(user);
    }


    private <T> T convertEntityToDTO(User user) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        return (T) modelMapper.map(user, UserAndPostDTO.class);
    }


}
