package com.minsoo.reservation.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.minsoo.reservation.shop.domain.Partner;
import com.minsoo.reservation.shop.model.PartnerLoginToken;
import com.minsoo.reservation.user.domain.User;
import com.minsoo.reservation.user.model.UserLoginToken;
import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@UtilityClass
public class JWTUtils {
    private final static String KEY = "minsoo";
    private final static String CLAIM_USER_ID = "user_id";

    public static UserLoginToken createToken(User user) {
        if (null == user) {
            return null;
        }
        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
        Date expiredDate = Timestamp.valueOf(expiredDateTime);

        String token = JWT.create()
                .withExpiresAt(expiredDate)
                .withClaim(CLAIM_USER_ID, user.getId())
                .withSubject(user.getUserName())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512(KEY.getBytes()));
        return UserLoginToken.builder()
                .token(token)
                .build();
    }

    public static PartnerLoginToken createToken(Partner partner) {
        if (null == partner) {
            return null;
        }
        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
        Date expiredDate = Timestamp.valueOf(expiredDateTime);

        String token = JWT.create()
                .withExpiresAt(expiredDate)
                .withClaim(CLAIM_USER_ID, partner.getId())
                .withSubject(partner.getPartnerName())
                .withIssuer(partner.getEmail())
                .sign(Algorithm.HMAC512(KEY.getBytes()));
        return PartnerLoginToken.builder()
                .token(token)
                .build();
    }

    public static String getIssuer(String token) {
        return JWT.require(Algorithm.HMAC512(KEY.getBytes()))
                .build()
                .verify(token)
                .getIssuer();
    }
}
