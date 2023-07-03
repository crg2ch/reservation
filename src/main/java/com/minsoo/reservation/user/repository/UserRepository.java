package com.minsoo.reservation.user.repository;

import com.minsoo.reservation.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    int countByEmail(String email);

    Optional<User> findByEmail(String email);
}
