package com.carspot.app.service;

import com.carspot.app.dto.UserReviewAndAvatarDTO;
import com.carspot.app.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    Review getUserReviewByEmailAddress(String emailAddress);

    Page<UserReviewAndAvatarDTO> getAllUserReviewsAndUserAvatarDTO(Pageable pageable);
    Review createUserReview(Review review, String emailAddress);
    void deleteUserReviewByReviewId(Long reviewId, String emailAddress);

}
