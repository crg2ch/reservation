package com.minsoo.reservation.shop.controller;

import com.minsoo.reservation.common.exception.ReservationNotFoundException;
import com.minsoo.reservation.common.model.ResponseResult;
import com.minsoo.reservation.shop.domain.Reservation;
import com.minsoo.reservation.shop.repository.ReservationRepository;
import com.minsoo.reservation.shop.type.StatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class ApiKioskController {
    private final ReservationRepository reservationRepository;

    @PutMapping("/api/shop/kiosk/{id}")
    public ResponseEntity<?> confirm(@PathVariable Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("예약 정보가 없습니다."));
        if (reservation.getStatus() == StatusType.WAIT || reservation.getStatus() == null) {
            return ResponseResult.fail("예약이 승인되지 않았습니다.(예약 대기)");
        } else if (reservation.getStatus() == StatusType.REJECT) {
            return ResponseResult.fail("예약이 승인되지 않았습니다.(예약 거절)");
        }
        LocalDateTime reservationTime = LocalDateTime.parse(reservation.getReservationTime());
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(reservationTime.minusMinutes(10))) {
            return ResponseResult.fail("예약 확인은 예약시간 10분 전부터 가능합니다.");
        }
        reservation.setUsed(true);
        reservationRepository.save(reservation);
        return ResponseResult.success("예약 확인에 성공하였습니다.");
    }
}
