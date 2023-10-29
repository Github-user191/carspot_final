package com.carspot.app.service.impl;

import com.carspot.app.dto.UserReviewAndAvatarDTO;
import com.carspot.app.entity.Review;
import com.carspot.app.entity.User;
import com.carspot.app.exception.exceptions.ReviewException;
import com.carspot.app.repository.ReviewRepository;
import com.carspot.app.repository.UserRepository;
import com.carspot.app.service.ReviewService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ReviewRepository reviewRepository;

    @Override
    public Review getUserReviewByEmailAddress(String emailAddress) {
        Review review = reviewRepository.findReviewByReviewerEmailAddress(emailAddress);

        if (review == null) {
            throw new ReviewException("You have not added a Review yet");
        }
        return review;
    }

    @Override
    public Page<UserReviewAndAvatarDTO> getAllUserReviewsAndUserAvatarDTO(Pageable pageable) {
        Page<UserReviewAndAvatarDTO> userReviewAndAvatarPage = reviewRepository.findAll(pageable)
                .map((object -> modelMapper.map(object, UserReviewAndAvatarDTO.class)));

        if (userReviewAndAvatarPage.isEmpty()) throw new ReviewException("There are no reviews at this time");
        return userReviewAndAvatarPage;
    }


    @Override
    public Review createUserReview(Review review, String emailAddress) {

        User user = userRepository.findUserByEmailAddress(emailAddress);
        if (user.getReview() != null) {
            if (user.getReview().getId() != null) {
                throw new ReviewException("You cannot have more than 1 review");
            }
        }
        review.setReviewerFullName(user.getFullName());
        review.setReviewerEmailAddress(user.getEmailAddress());
        review.setUser(user);


        reviewRepository.save(review);


        return review;
    }

    @Override
    public void deleteUserReviewByReviewId(Long reviewId, String emailAddress) {
        Review review = reviewRepository.findReviewById(reviewId);

        if (review == null) {
            throw new ReviewException("Review with id " + reviewId + " was not found");
        }

        if (!review.getReviewerEmailAddress().equals(emailAddress)) {
            throw new ReviewException("Review not found in your account");
        }
        reviewRepository.deleteById(reviewId);
    }
}
