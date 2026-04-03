package com.precisioncast.erp.auth.repository;

import com.precisioncast.erp.auth.entity.PasswordResetToken;
import com.precisioncast.erp.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUser(User user);
}
