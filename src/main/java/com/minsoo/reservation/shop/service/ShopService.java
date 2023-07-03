package com.minsoo.reservation.shop.service;

import com.minsoo.reservation.common.model.ServiceResult;
import com.minsoo.reservation.shop.model.ShopInput;

public interface ShopService {
    ServiceResult add(String email, ShopInput shopInput);

    ServiceResult approve(Long id);

    ServiceResult reject(Long id);
}
