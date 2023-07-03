package com.minsoo.reservation.review.repository;

import com.minsoo.reservation.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    long countByReservationId(Long reservationId);

    Optional<Review> findByReservationId(Long reservationId);
}
