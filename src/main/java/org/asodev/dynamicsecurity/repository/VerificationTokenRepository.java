package org.asodev.dynamicsecurity.repository;

import org.asodev.dynamicsecurity.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    VerificationToken findByUserId(Long userId);
}
