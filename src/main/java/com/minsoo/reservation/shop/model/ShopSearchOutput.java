package com.minsoo.reservation.shop.model;

import com.minsoo.reservation.shop.domain.Shop;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopSearchOutput {
    private String shopName;

    private String location;

    private String partnerName;

    private String phone;

    private String detail;

    public static ShopSearchOutput of(Shop shop) {
        return ShopSearchOutput.builder()
                .shopName(shop.getShopName())
                .location(shop.getLocation())
                .partnerName(shop.getPartner().getPartnerName())
                .phone(shop.getPartner().getPhone())
                .detail(shop.getDetail())
                .build();
    }

}
