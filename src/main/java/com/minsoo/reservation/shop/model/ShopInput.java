package com.minsoo.reservation.shop.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopInput {
    @NotBlank(message = "이름은 필수 항목 입니다.")
    private String shopName;

    @NotBlank(message = "장소는 필수 항목 입니다.")
    private String location;

    private String detail;
}
