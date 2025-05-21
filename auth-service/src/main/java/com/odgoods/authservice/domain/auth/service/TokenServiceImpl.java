package com.odgoods.authservice.domain.auth.service;

import java.util.List;
import java.util.Optional;

import com.odgoods.authservice.domain.auth.entity.Token;
import com.odgoods.authservice.domain.auth.entity.User;
import com.odgoods.authservice.domain.auth.model.TokenType;
import com.odgoods.authservice.domain.auth.repository.TokenRepository;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public void saveToken(String tokenStr, TokenType tokenType, boolean isExpired, boolean isRevoked, User user) {
        Token token = Token.builder()
                .token(tokenStr)
                .tokenType(tokenType)
                .isExpired(isExpired)
                .isRevoked(isRevoked)
                .user(user)
                .build();

        // revoke previous tokens for the user
        revokeAllTokensForUser(user);

        tokenRepository.save(token);
    }

    @Override
    public void revokeAllTokensForUser(User user) {
        List<Token> validTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (!validTokens.isEmpty()) {
            validTokens.forEach(token -> {
                token.setIsExpired(true);
                token.setIsRevoked(true);
            });
            tokenRepository.saveAll(validTokens);
        }
    }

    @Override
    public boolean findByToken(String jwtToken) {
        Optional<Token> tokenOptional = tokenRepository.findByToken(jwtToken);
        if (tokenOptional.isPresent()) {
            Token token = tokenOptional.get();
            return !token.getIsExpired() && !token.getIsRevoked();
        }
        return false;
    }

}
