package com.carspot.app.service.impl;

import com.carspot.app.dto.UserAndPostDTO;
import com.carspot.app.entity.Review;
import com.carspot.app.entity.User;
import com.carspot.app.entity.UserAvatar;
import com.carspot.app.exception.exceptions.EmailAlreadyExistsException;
import com.carspot.app.exception.exceptions.FileUploadException;
import com.carspot.app.exception.exceptions.PasswordException;
import com.carspot.app.payload.request.UpdateAccountDetailsRequest;
import com.carspot.app.repository.PostImageRepository;
import com.carspot.app.repository.ReviewRepository;
import com.carspot.app.repository.UserAvatarRepository;
import com.carspot.app.repository.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewServiceImpl reviewService;

    @Mock
    private RoleServiceImpl roleService;

    @Mock
    private PostImageRepository postImageRepository;

    @Mock
    private UserAvatarRepository userAvatarRepository;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FileUploadServiceImpl fileUploadService;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Mock
    private ModelMapper modelMapper;

    private User user;

    @BeforeEach
    void setUp() {

        this.passwordEncoder = new BCryptPasswordEncoder();

        userService = new UserServiceImpl(userRepository, passwordEncoder, roleService, reviewRepository, fileUploadService, modelMapper,
                null, null);


        user = new User(1, "admin", "admin@gmail.com", "+27339923823", passwordEncoder.encode("password"));


    }

    @AfterEach
    void tearDown() {

    }


    @Test
    void testIfUserExistsByUserId() {
        when(userRepository.getById(1L)).thenReturn(user);
        User theUser = userService.findUserById(user.getId());

        assertNotNull(theUser);
        assertEquals(user.getId(), theUser.getId());
        assertEquals(user.getEmailAddress(), theUser.getEmailAddress());
    }

    @Test
    void testIfUserDoesNotExistByUserId() {

        long id = 9999L;

        when(userRepository.getById(anyLong())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> userService.findUserById(id), "User with id " + id + " does not exists");


        verify(userRepository, times(1)).getById(id);
    }

    @Test
    void testIfUserExistsByEmailAddress() {
        String emailAddress = "admin@gmail.com";
        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);


        User theUser = userService.findUserByEmailAddress(emailAddress);
        assertEquals(1, theUser.getId());
        assertEquals(emailAddress, theUser.getEmailAddress());
        assertEquals(user.getPassword(), theUser.getPassword());


    }

    @Test
    void testIfUserDoesNotExistByEmailAddress() {
        String emailAddress = "admin@gmail.com";

        assertThrows(UsernameNotFoundException.class,
                () ->  userService.findUserByEmailAddress(emailAddress), "User with email address " + emailAddress + " does not exist");

        verify(userRepository).findUserByEmailAddress(emailAddress);

    }

    @Test
    void testIfUserCanRegisterSuccessfully() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User created = userService.register(user);

        assertNotNull(created);
        assertEquals("admin@gmail.com", created.getEmailAddress());
        verify(userRepository).save(any());
    }

    @Test
    void testIfUserEmailAddressAlreadyTakenWhenRegistering() {
        given(userRepository.existsByEmailAddress(user.getEmailAddress())).willReturn(true);

        assertThatThrownBy(() -> userService.register(user))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Account already exists with this email");

        // verify method userRepository.save() was never called, because execution terminated
        verify(userRepository, never()).save(any());
    }

    @Test
    void testIfUserCanChangePasswordSuccessfully() {
        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);

        User theUser = userService.findUserByEmailAddress("admin@gmail.com");

        // email, old password, new password
        userService.changePassword("admin@gmail.com", "password", "password123");
        assertTrue(passwordEncoder.matches("password123", theUser.getPassword()));
        verify(userRepository).save(any());
    }


    @Test
    void testIfUserOldPasswordDoesNotMatchCurrentPassword(){
        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);

        assertThatThrownBy(() -> userService.changePassword("admin@gmail.com", "fakepassword", "password123"))
                .isInstanceOf(PasswordException.class)
                .hasMessageContaining("Old password is invalid");

        verify(userRepository, never()).save(any());
    }


    @Test
    void testCanUpdateDetailsAndUpdateReviewerDetailsAndUploadAvatar() throws Exception {
        MockMultipartFile avatar = new MockMultipartFile("avatar", "test.jpg", "image/jpg", "Some bytes".getBytes());

        Review review = new Review(1L, "user", "user@gmail.com", "This is my first review of the website!");
        when(reviewRepository.findReviewByReviewerEmailAddress(review.getReviewerEmailAddress())).thenReturn(review);

        when(userRepository.getById(1L)).thenReturn(user);


        String fileName = new Date().getTime() + "-" + Objects.requireNonNull(avatar.getOriginalFilename()).replace(" ", "_");
        String extension = FilenameUtils.getExtension(avatar.getOriginalFilename());

        UserAvatar userAvatar = new UserAvatar("44", "333", user);

        user.setEmailAddress("user@gmail.com");
        user.setFullName("user");
        user.setAvatar(userAvatar);

        User updatedUser = userService.updateUser(
            new UpdateAccountDetailsRequest(user.getId(), user.getFullName(), user.getEmailAddress(), user.getMobileNumber()),
                avatar);

        assertNotNull(updatedUser.getAvatar());
        assertEquals("user", review.getReviewerFullName());
        assertEquals("user@gmail.com", review.getReviewerEmailAddress());
        assertEquals("user@gmail.com", updatedUser.getEmailAddress());
        assertEquals("user", updatedUser.getFullName());
        verify(reviewRepository).save(any(Review.class));
        verify(userRepository).save(any(User.class));

    }



    @Test
    void testCanUpdateDetailsAndUpdateReviewerDetails() throws IOException {
        Review review = new Review(1L, "user", "user@gmail.com", "This is my first review of the website!");
        when(reviewRepository.findReviewByReviewerEmailAddress(review.getReviewerEmailAddress())).thenReturn(review);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.getById(1L)).thenReturn(user);


        user.setEmailAddress("user@gmail.com");
        user.setFullName("user");


        User updatedUser = userService.updateUser(
                new UpdateAccountDetailsRequest(user.getId(), user.getFullName(), user.getEmailAddress(), user.getMobileNumber()), null);

        assertEquals("user", review.getReviewerFullName());
        assertEquals("user@gmail.com", review.getReviewerEmailAddress());
        assertEquals("user@gmail.com", updatedUser.getEmailAddress());
        assertEquals("user", updatedUser.getFullName());
        verify(reviewRepository).save(any(Review.class));
        verify(userRepository).save(any(User.class));
    }


    @Test
    @Disabled
    public void testCanGetUserAndPostInformation() {

        when(userRepository.findUserByEmailAddress("admin@gmail.com")).thenReturn(user);



        UserAndPostDTO userAndPostDTO = userService.getUserAndPostInfo("admin@gmail.com");
        System.out.println(userAndPostDTO);


    }



}