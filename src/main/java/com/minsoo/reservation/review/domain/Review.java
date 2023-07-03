package com.minsoo.reservation.review.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long reservationId;

    @Column
    private String shopName;

    @Column
    private String userEmail;

    @Column
    private int rate;

    @Column
    private String detail;

    @Column
    private LocalDateTime regDate;

    @Column
    private LocalDateTime editDate;
}
