package com.minsoo.reservation.review.service;

import com.minsoo.reservation.common.model.ServiceResult;
import com.minsoo.reservation.review.domain.Review;
import com.minsoo.reservation.review.model.ReviewInput;

import java.util.List;

public interface ReviewService {
    ServiceResult review(String userEmail, ReviewInput reviewInput);

    ServiceResult editReview(String userEmail, ReviewInput reviewInput);

    List<Review> list();
}
