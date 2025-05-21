package com.odgoods.authservice.domain.auth.repository;

import java.util.List;
import java.util.Optional;

import com.odgoods.authservice.domain.auth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
                SELECT t FROM Token t
                WHERE t.user.id = :userId
                AND t.isExpired = false
                AND t.isRevoked = false
            """)
    List<Token> findAllValidTokensByUser(@Param("userId") Long userId);

    Optional<Token> findByToken(String token);
}
