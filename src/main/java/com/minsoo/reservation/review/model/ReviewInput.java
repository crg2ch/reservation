package com.minsoo.reservation.review.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewInput {
    @NotNull(message = "예약 번호를 입력해주세요")
    private Long reservationId;

    @NotNull(message = "별점을 입력해주세요")
    @Min(value = 1, message = "별점은 1~5점 사이 정수여야 합니다.")
    @Max(value = 5, message = "별점은 1~5점 사이 정수여야 합니다.")
    private Integer rate;

    private String detail;
}
