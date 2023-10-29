package com.carspot.app.service.impl;

import com.carspot.app.dto.UserReviewAndAvatarDTO;
import com.carspot.app.entity.Review;
import com.carspot.app.entity.User;
import com.carspot.app.exception.exceptions.ReviewException;
import com.carspot.app.repository.ReviewRepository;
import com.carspot.app.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    private Review reviewOne, reviewTwo;
    private UserReviewAndAvatarDTO userReviewAndAvatarDTOOne, userReviewAndAvatarDTOTwo;
    private User user;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {

        reviewService = new ReviewServiceImpl(userRepository, modelMapper, reviewRepository);
        reviewOne = new Review(1L, "admin", "admin@gmail.com", "This is my first review of the website!");
        reviewTwo = new Review(2L, "user", "user@gmail.com", "This is my second review of the website!");
        user = new User(1, "admin", "admin@gmail.com", "+27339923823", "password");

        userReviewAndAvatarDTOOne = new UserReviewAndAvatarDTO(1L, "admin", "admin@gmail.com", LocalDateTime.now(),
                "This is my first review of the website!", null);


    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void canFindUserReviewByEmailAddress() {

        when(reviewRepository.findReviewByReviewerEmailAddress(anyString())).thenReturn(reviewOne);

        Review foundReview = reviewService.getUserReviewByEmailAddress("admin@gmail.com");

        assertNotNull(foundReview);
        assertEquals(1, foundReview.getId());
        assertEquals("admin@gmail.com", foundReview.getReviewerEmailAddress());
    }

    @Test
    void willThrowIfCannotFindUserReviewByEmailAddress() {

        when(reviewRepository.findReviewByReviewerEmailAddress(anyString())).thenReturn(null);

        assertThrows(ReviewException.class,
                () -> reviewService.getUserReviewByEmailAddress("admin@gmail.com"), "You have not added a Review yet");


        verify(reviewRepository, times(1)).findReviewByReviewerEmailAddress(anyString());
    }


//    @Test
//    void testCanGetAllUserReviews() throws IOException {
//        MockMultipartFile avatar1 = new MockMultipartFile("avatar", "test.jpg", "image/jpg", new ClassPathResource("test.jpg").getInputStream());
//        MockMultipartFile avatar2 = new MockMultipartFile("avatar", "test.jpg", "image/jpg", new ClassPathResource("test.jpg").getInputStream());
//
//        UserAvatar userAvatar1 = new UserAvatar(StringUtils.cleanPath(avatar1.getOriginalFilename()), StringUtils.cleanPath(avatar1.getContentType()),avatar1.getBytes());
//        UserAvatar userAvatar2 = new UserAvatar(StringUtils.cleanPath(avatar2.getOriginalFilename()), StringUtils.cleanPath(avatar2.getContentType()),avatar2.getBytes());
//
////        userReviewAndAvatarDTOOne.setUserAvatar(userAvatar1);
////        userReviewAndAvatarDTOTwo.setUserAvatar(userAvatar2);
////
////        List<Review> reviewList = List.of(reviewOne, reviewTwo);
////
////        Page<UserReviewAndAvatarDTO> userReviewAndAvatarPage = new PageImpl<>(List.of(userReviewAndAvatarDTOOne, userReviewAndAvatarDTOTwo));
//
//        Page<UserReviewAndAvatarDTO> page = reviewService.getAllUserReviewsAndUserAvatarDTO(PageRequest.ofSize(10));
//        when(modelMapper.map(any(), any())).thenReturn(page);
//        //reviewService.getAllUserReviewsAndUserAvatarDTO(PageRequest.ofSize(10));
//
//
//
//        assertThat(page.getTotalElements()).isEqualTo(2);
//    }


    @Test
    void testCanCreateUserReviewSuccessfully() {

        when(userRepository.findUserByEmailAddress("admin@gmail.com")).thenReturn(user);

        // Should set the name and email automatically when saving the review
        Review createdReview = new Review(1L, null, null, "pppppppp");
        when(reviewRepository.save(any(Review.class))).thenReturn(createdReview);

        reviewService.createUserReview(createdReview, "admin@gmail.com");
        user.setReview(createdReview);

        assertThat(user.getReview()).isNotNull();
        assertEquals("admin@gmail.com", createdReview.getReviewerEmailAddress());
        assertEquals("admin", createdReview.getReviewerFullName());
    }

    @Test
    void testCannotCreateReviewBecauseUserHasExistingReview() {
        when(userRepository.findUserByEmailAddress("admin@gmail.com")).thenReturn(user);

        user.setReview(reviewOne);

        assertThrows(ReviewException.class,
                () -> reviewService.createUserReview(reviewOne, "admin@gmail.com"),
                "You cannot have more than 1 review");

        verify(userRepository).findUserByEmailAddress(anyString());
        verify(reviewRepository, never()).save(any(Review.class));

    }

    @Test
    void testCanDeleteUserReviewById() {
        when(reviewRepository.findReviewById(anyLong())).thenReturn(reviewOne);

        // Arrange
        doNothing().when(reviewRepository).deleteById(anyLong());

        // Act & Assert
        reviewService.deleteUserReviewByReviewId(1L, "admin@gmail.com");


        verify(reviewRepository, times(1)).deleteById(anyLong());

    }

    @Test
    void willThrowIfUserReviewDoesNotExistWhenAttemptingToDelete() {
        long reviewId = 999;
        when(reviewRepository.findReviewById(reviewId)).thenReturn(null);

        assertThrows(ReviewException.class, () -> reviewService.deleteUserReviewByReviewId(reviewId, "admin@gmail.com"),
                "Review with id " + reviewId + " was not found");

        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void willThrowIfUserReviewEmailIsNotSameAsLoggedInUser() {
        when(reviewRepository.findReviewById(anyLong())).thenReturn(reviewOne);
        // Checking if the review belongs to the user fake@gmail.com, will throw exception because the review doesn't
        // belong to them
        assertThrows(ReviewException.class, () -> reviewService.deleteUserReviewByReviewId(anyLong(),
    "fake@gmail.com"), "Review not found in your account");

        verify(reviewRepository, never()).deleteById(anyLong());


    }
}