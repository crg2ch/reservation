package com.minsoo.reservation.shop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Partner partner;

    @Column(unique = true)
    @NotBlank(message = "상점 이름을 입력해주세요")
    private String shopName;

    @Column
    private String location;

    @Column
    private String detail;
}
