package org.asodev.dynamicsecurity.repository;

import org.asodev.dynamicsecurity.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    List<Token> findAllByUser_IdAndRevokedFalse(Long userId);

    boolean existsByTokenAndRevokedFalse(String token);

    @Modifying
    @Transactional
    @Query("UPDATE Token t SET t.revoked = true WHERE t.token = :token")
    void revokeToken(String token);

    @Modifying
    @Transactional
    @Query("UPDATE Token t SET t.revoked = true WHERE t.user.id = :userId")
    void revokeAllUserTokens(Long userId);
}