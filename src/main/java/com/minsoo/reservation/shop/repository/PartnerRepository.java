package com.minsoo.reservation.shop.repository;

import com.minsoo.reservation.shop.domain.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    int countByEmail(String email);

    Optional<Partner> findByEmail(String email);
}
