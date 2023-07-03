package com.minsoo.reservation.common.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMessageHeader {
    private boolean result;
    private String resultCode;
    private String message;
    private int status;
}
