package com.odgoods.apigateway.filter;

import com.odgoods.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class JwtAuthenticationFilter implements GatewayFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header");
            return unauthorized(exchange, "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        Claims claims;

        try {
            jwtUtil.validateToken(token);
            claims = jwtUtil.extractClaims(token);
            log.debug("JWT validated successfully: {}", claims);
        } catch (JwtException e) {
            log.error("JWT validation error: {}", e.getMessage());
            return unauthorized(exchange, "Invalid JWT: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during JWT processing: ", e);
            return unauthorized(exchange, "Unexpected error during token validation.");
        }

        Long userId;
        try {
            userId = claims.get("id", Long.class);
            if (userId == null) {
                log.warn("Missing 'id' claim in JWT");
                return unauthorized(exchange, "Missing user ID in token.");
            }
        } catch (Exception e) {
            log.error("Error extracting user ID from claims: ", e);
            return unauthorized(exchange, "Invalid user ID in token.");
        }

        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(builder -> builder.header("X-User-Id", String.valueOf(userId)))
                .build();

        return chain.filter(modifiedExchange);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "text/plain");

        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }
}
