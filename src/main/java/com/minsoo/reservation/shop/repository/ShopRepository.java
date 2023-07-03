package com.minsoo.reservation.shop.repository;

import com.minsoo.reservation.shop.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    long countByShopName(String shopName);

    Optional<Shop> findByShopName(String shopName);
}
