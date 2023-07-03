package com.minsoo.reservation.user.service;

import com.minsoo.reservation.common.MailComponent;
import com.minsoo.reservation.common.exception.UserNotFoundException;
import com.minsoo.reservation.common.model.ServiceResult;
import com.minsoo.reservation.shop.domain.Reservation;
import com.minsoo.reservation.shop.domain.Shop;
import com.minsoo.reservation.user.model.ReservationInput;
import com.minsoo.reservation.shop.repository.ReservationRepository;
import com.minsoo.reservation.shop.repository.ShopRepository;
import com.minsoo.reservation.shop.type.StatusType;
import com.minsoo.reservation.user.domain.User;
import com.minsoo.reservation.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final ReservationRepository reservationRepository;
    private final TemplateEngine templateEngine;
    private final MailComponent mailComponent;

    @Override
    public ServiceResult reserve(String userEmail, ReservationInput reservationInput) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));
        Shop shop = shopRepository.findByShopName(reservationInput.getShopName())
                .orElseThrow(() -> new UserNotFoundException("매장 정보가 없습니다."));

        LocalDateTime reservationTime = LocalDateTime.of(reservationInput.getDate(), reservationInput.getTime());
        System.out.println("reservationTime: " + reservationTime);

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .shopName(shop.getShopName())
                .userEmail(user.getEmail())
                .userPhone(user.getPhone())
                .status(StatusType.WAIT)
                .reservationTime(reservationTime.toString())
                .build());
        String title = String.format("%s님의 예약 요청입니다.", user.getUserName());
        Context context = new Context();
        context.setVariable("RESERVATION_ID", reservation.getId());
        context.setVariable("SHOP_NAME", shop.getShopName());
        context.setVariable("USER_EMAIL", userEmail);
        context.setVariable("RESERVATION_TIME", reservationTime.toString());
        String emailContent = templateEngine.process("mail/reservation", context);
        mailComponent.sendMail(userEmail, user.getUserName(), shop.getPartner().getEmail(), shop.getShopName(), title, emailContent);
        return ServiceResult.success("예약을 요청하였습니다.");
    }
}
