package com.minsoo.reservation.review.controller;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.minsoo.reservation.common.exception.ReviewNotFoundException;
import com.minsoo.reservation.common.model.ResponseError;
import com.minsoo.reservation.common.model.ResponseResult;
import com.minsoo.reservation.common.model.ServiceResult;
import com.minsoo.reservation.review.domain.Review;
import com.minsoo.reservation.review.model.ReviewInput;
import com.minsoo.reservation.review.repository.ReviewRepository;
import com.minsoo.reservation.review.service.ReviewService;
import com.minsoo.reservation.util.JWTUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ApiReviewController {
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    @PostMapping("/api/review")
    public ResponseEntity<?> review(@RequestHeader("TOKEN") String token,
                                    @RequestBody @Valid ReviewInput reviewInput,
                                    Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> responseErrorList.add(ResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }
        String userEmail;
        try {
            userEmail = JWTUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("사용자의 토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = reviewService.review(userEmail, reviewInput);
        return ResponseResult.result(result);
    }

    @GetMapping("/api/review")
    public ResponseEntity<?> getReviews() {
        List<Review> list = reviewService.list();
        return ResponseResult.success(list);
    }

    @GetMapping("/api/review/{id}")
    public ResponseEntity<?> getReview(@PathVariable Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰가 존재하지 않습니다."));
        return ResponseResult.success(review);
    }

    @PutMapping("/api/review/edit")
    public ResponseEntity<?> editReview(@RequestHeader("TOKEN") String token,
                                        @RequestBody @Valid ReviewInput reviewInput,
                                        Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> responseErrorList.add(ResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }
        String userEmail;
        try {
            userEmail = JWTUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("사용자의 토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = reviewService.editReview(userEmail, reviewInput);
        return ResponseResult.result(result);
    }

    @DeleteMapping("/api/review/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id,
                                          @RequestHeader("TOKEN") String token) {
        try {
            JWTUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("사용자의 토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰가 존재하지 않습니다."));
        reviewRepository.delete(review);
        return ResponseResult.success("리뷰를 삭제하였습니다.");
    }
}
