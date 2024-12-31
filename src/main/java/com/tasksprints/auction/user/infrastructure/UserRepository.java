package com.tasksprints.auction.user.infrastructure;

import com.tasksprints.auction.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM users u JOIN FETCH u.wallet w WHERE u.id = :id")
    Optional<User> findByIdWithWallet(@Param("id") Long id);
}
