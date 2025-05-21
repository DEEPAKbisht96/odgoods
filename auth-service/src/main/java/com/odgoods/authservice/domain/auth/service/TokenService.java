package com.odgoods.authservice.domain.auth.service;


import com.odgoods.authservice.domain.auth.entity.User;
import com.odgoods.authservice.domain.auth.model.TokenType;

public interface TokenService {

    void saveToken(String tokenStr, TokenType tokenType, boolean isExpired, boolean isRevoked, User user);

    void revokeAllTokensForUser(User user);

    boolean findByToken(String token);
}
