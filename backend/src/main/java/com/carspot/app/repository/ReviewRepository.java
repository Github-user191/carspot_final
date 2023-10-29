package com.carspot.app.repository;

import com.carspot.app.entity.Post;
import com.carspot.app.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findReviewById(long id);
    Review findReviewByReviewerEmailAddress(String emailAddress);
    List<Review> findAllByReviewerEmailAddress(String emailAddress);
}
