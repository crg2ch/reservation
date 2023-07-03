package com.minsoo.reservation.shop.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationInput {
    @NotBlank(message = "매장 이름을 입력해주세요.")
    private String shopName;


    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @NotNull(message = "날짜를 입력해주세요. yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    @NotNull(message = "시간을 입력해주세요. HH:mm:ss")
    private LocalTime time;
}
