package com.minsoo.reservation.user.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginToken {
    private String token;
}
