package com.minsoo.reservation.common.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceResult {
    private boolean result;
    private String message;

    public static ServiceResult fail(String s) {
        return ServiceResult.builder()
                .message(s)
                .result(false)
                .build();
    }

    public static ServiceResult success(String s) {
        return ServiceResult.builder()
                .message(s)
                .result(true)
                .build();
    }

    public boolean isFail() {
        return !result;
    }
}
