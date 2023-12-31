package com.minsoo.reservation.common.model;

import lombok.*;
import org.springframework.validation.FieldError;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseError {
    private String field;
    private String message;

    public static ResponseError of(FieldError error) {
        return ResponseError.builder()
                .field(error.getField())
                .message(error.getDefaultMessage())
                .build();
    }
}
