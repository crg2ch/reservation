package com.minsoo.reservation.review.service;

import com.minsoo.reservation.common.exception.ReservationNotFoundException;
import com.minsoo.reservation.common.model.ServiceResult;
import com.minsoo.reservation.review.domain.Review;
import com.minsoo.reservation.review.model.ReviewInput;
import com.minsoo.reservation.review.repository.ReviewRepository;
import com.minsoo.reservation.shop.domain.Reservation;
import com.minsoo.reservation.shop.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ServiceResult review(String userEmail, ReviewInput reviewInput) {
        Reservation reservation = reservationRepository.findById(reviewInput.getReservationId())
                .orElseThrow(() -> new ReservationNotFoundException("예약 정보가 없습니다."));
        if (!reservation.isUsed()) {
            return ServiceResult.fail("리뷰는 예약확인 처리된 예약에만 작성할 수 있습니다.");
        }

        if (reviewRepository.countByReservationId(reviewInput.getReservationId()) > 0) {
            return ServiceResult.fail("이미 작성된 리뷰가 있습니다.");
        }

        reviewRepository.save(Review.builder()
                .reservationId(reviewInput.getReservationId())
                .shopName(reservation.getShopName())
                .userEmail(userEmail)
                .rate(reviewInput.getRate())
                .detail(reviewInput.getDetail())
                .regDate(LocalDateTime.now())
                .build());
        return ServiceResult.success("리뷰를 등록하였습니다.");
    }

    @Override
    public ServiceResult editReview(String userEmail, ReviewInput reviewInput) {
        reservationRepository.findById(reviewInput.getReservationId())
                .orElseThrow(() -> new ReservationNotFoundException("예약 정보가 없습니다."));

        Optional<Review> optionalReview = reviewRepository.findByReservationId(reviewInput.getReservationId());
        if (optionalReview.isEmpty()) {
            return ServiceResult.fail("작성된 리뷰가 없습니다.");
        }

        Review review = optionalReview.get();
        review.setUserEmail(userEmail);
        review.setRate(reviewInput.getRate());
        review.setDetail(reviewInput.getDetail());
        review.setEditDate(LocalDateTime.now());

        reviewRepository.save(review);
        return ServiceResult.success("리뷰를 수정하였습니다.");
    }

    @Override
    public List<Review> list() {
        return reviewRepository.findAll();
    }
}
