package com.odgoods.apigateway.config;

import com.odgoods.apigateway.filter.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public GatewayConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

                // === Public auth routes (no filter) ===
                .route("auth-login", r -> r.path("/auth/login")
                        .uri("http://auth-service:8001"))
                .route("auth-register", r -> r.path("/auth/register")
                        .uri("http://auth-service:8001"))
                .route("auth-refresh", r -> r.path("/auth/refresh-token")
                        .uri("http://auth-service:8001"))
                .route("auth-health", r -> r.path("/auth/health")
                        .uri("http://auth-service:8001"))

                // === Protected auth routes ===
                .route("auth-other", r -> r.path("/auth/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("http://auth-service:8001"))

                // === Protected product routes ===
                .route("product-service", r -> r.path("/products/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("http://product-service:8002"))

                // === Protected order routes ===
                .route("order-service", r -> r.path("/orders/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("http://order-service:8003"))

                .build();
    }
}