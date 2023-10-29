package com.carspot.app.controller;

import com.carspot.app.dto.UserReviewAndAvatarDTO;
import com.carspot.app.entity.Review;
import com.carspot.app.service.ReviewService;
import com.carspot.app.service.impl.ErrorValidationServiceImpl;
import com.carspot.app.service.impl.ReviewServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final ErrorValidationServiceImpl errorValidationService;

    public ReviewController(ReviewService reviewService, ErrorValidationServiceImpl errorValidationService) {
        this.reviewService = reviewService;
        this.errorValidationService = errorValidationService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrUpdateUserReview(@Valid @RequestBody Review review, BindingResult result, Principal principal) {
        ResponseEntity<?> errorMap = errorValidationService.validationService(result);
        if(errorMap != null) return errorMap;

        reviewService.createUserReview(review, principal.getName());

        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }



    @GetMapping("/all")
    public ResponseEntity<?> getAllUserReviews(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "5", required = false) int pageSize
    )  {

        Pageable pagingSort = PageRequest.of(pageNo, pageSize);
        Page<UserReviewAndAvatarDTO> pageTuts = reviewService.getAllUserReviewsAndUserAvatarDTO(pagingSort);

        Map<Object, Object> response = new HashMap<>();
        response.put("reviews", pageTuts.getContent());
        response.put("currentPage", pageTuts.getNumber());
        response.put("totalItems", pageTuts.getTotalElements());
        response.put("totalPages", pageTuts.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<?> getUserReviewByEmailAddress(Principal principal)  {

        Review review = reviewService.getUserReviewByEmailAddress(principal.getName());

        return ResponseEntity.status(HttpStatus.OK).body(review);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteUserReview(@PathVariable Long reviewId, Principal principal)  {
        reviewService.deleteUserReviewByReviewId(reviewId, principal.getName());
        return new ResponseEntity<String>("Review with id " + reviewId + " was deleted successfully!", HttpStatus.OK);
    }
}
