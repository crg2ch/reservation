package com.minsoo.reservation.shop.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationApproval {
    @NotNull(message = "예약 번호를 입력해주세요")
    private Long reservationId;
}
