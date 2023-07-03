package com.minsoo.reservation.shop.domain;


import com.minsoo.reservation.shop.type.StatusType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String shopName;

    @Column
    private String userEmail;

    @Column
    private String userPhone;

    @Column
    private String reservationTime;

    @Column
    private StatusType status;

    @Column
    private boolean used;
}
