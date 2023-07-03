package com.minsoo.reservation.user.service;

import com.minsoo.reservation.common.model.ServiceResult;
import com.minsoo.reservation.user.model.ReservationInput;

public interface UserService {
    ServiceResult reserve(String userEmail, ReservationInput reservationInput);
}
