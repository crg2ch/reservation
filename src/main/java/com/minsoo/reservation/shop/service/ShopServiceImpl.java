package com.minsoo.reservation.shop.service;

import com.minsoo.reservation.common.exception.ExistsEmailException;
import com.minsoo.reservation.common.exception.PartnerNotFoundException;
import com.minsoo.reservation.common.exception.ReservationNotFoundException;
import com.minsoo.reservation.common.model.ServiceResult;
import com.minsoo.reservation.shop.domain.Partner;
import com.minsoo.reservation.shop.domain.Reservation;
import com.minsoo.reservation.shop.domain.Shop;
import com.minsoo.reservation.shop.model.ShopInput;
import com.minsoo.reservation.shop.repository.PartnerRepository;
import com.minsoo.reservation.shop.repository.ReservationRepository;
import com.minsoo.reservation.shop.repository.ShopRepository;
import com.minsoo.reservation.shop.type.StatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final PartnerRepository partnerRepository;
    private final ShopRepository shopRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public ServiceResult add(String email, ShopInput shopInput) {
        Partner partner = partnerRepository.findByEmail(email)
                .orElseThrow(() -> new PartnerNotFoundException("파트너 정보가 없습니다."));

        if (shopRepository.countByShopName(shopInput.getShopName()) > 0) {
            throw new ExistsEmailException("동일한 이름의 매장이 있습니다.");
        }
        shopRepository.save(Shop.builder()
                .partner(partner)
                .shopName(shopInput.getShopName())
                .location(shopInput.getLocation())
                .detail(shopInput.getDetail())
                .build());
        return ServiceResult.success("매장을 추가하였습니다.");
    }

    @Override
    public ServiceResult approve(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("예약 정보가 없습니다."));
        reservation.setStatus(StatusType.APPROVE);
        reservationRepository.save(reservation);
        return ServiceResult.success("예약 승인이 완료되었습니다.");
    }

    @Override
    public ServiceResult reject(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("예약 정보가 없습니다."));
        reservation.setStatus(StatusType.REJECT);
        reservationRepository.save(reservation);
        return ServiceResult.success("예약이 거절되었습니다.");
    }
}
