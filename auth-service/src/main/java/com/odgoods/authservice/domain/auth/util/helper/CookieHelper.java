package com.odgoods.authservice.domain.auth.util.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;

@Component
public class CookieHelper {

    @Value("${app.environment:dev}")
    private String environment;

    public Cookie createSecureCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure("prod".equalsIgnoreCase(environment));
        cookie.setPath("/auth/refresh-token");
        cookie.setMaxAge(30 * 24 * 60 * 60);
        return cookie;
    }
}
