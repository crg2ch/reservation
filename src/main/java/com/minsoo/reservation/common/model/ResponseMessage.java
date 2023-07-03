package com.minsoo.reservation.common.model;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMessage {

    ResponseMessageHeader header;
    Object body;

    public static ResponseMessage fail(String message) {
        return fail(message, null);
    }


    public static ResponseMessage fail(String message, Object data) {
        return ResponseMessage.builder()
                .header(ResponseMessageHeader.builder()
                        .result(false)
                        .resultCode("")
                        .message(message)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build())
                .body(data)
                .build();
    }

    public static ResponseMessage success(Object data) {
        return ResponseMessage.builder()
                .header(ResponseMessageHeader.builder()
                        .result(true)
                        .resultCode("")
                        .message("")
                        .status(HttpStatus.OK.value())
                        .build())
                .body(data)
                .build();
    }

    public static ResponseMessage success() {
        return success(null);
    }
}
