package com.odgoods.authservice.domain.auth.controller;

import com.odgoods.authservice.domain.auth.dto.AuthenticationResponse;
import com.odgoods.authservice.domain.auth.dto.LoginRequest;
import com.odgoods.authservice.domain.auth.dto.RegisterRequest;
import com.odgoods.authservice.domain.auth.dto.UserResponse;
import com.odgoods.authservice.domain.auth.service.AuthService;
import com.odgoods.authservice.domain.auth.util.helper.CookieHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CookieHelper cookieHelper;

    public AuthController(AuthService authService, CookieHelper cookieHelper) {
        this.authService = authService;
        this.cookieHelper = cookieHelper;
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.status(HttpStatus.CREATED).body("auth service working");
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Validated @RequestBody RegisterRequest request,
                                                           HttpServletResponse response) {
        AuthenticationResponse authResponse = authService.register(request);

        // Set cookie in controller
        Cookie refreshCookie = cookieHelper.createSecureCookie(
                "refreshToken",
                authResponse.getRefreshToken());
        response.addCookie(refreshCookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Validated @RequestBody LoginRequest request,
            HttpServletResponse response) {
        AuthenticationResponse authResponse = authService.login(request);

        // Set cookie in controller
        Cookie refreshCookie = cookieHelper.createSecureCookie(
                "refreshToken",
                authResponse.getRefreshToken());
        response.addCookie(refreshCookie);

        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        UserResponse userResponse = authService.me();

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {


        AuthenticationResponse authResponse = authService.refreshToken(refreshToken);

        // Set cookie in controller
        Cookie refreshCookie = cookieHelper.createSecureCookie(
                "refreshToken",
                authResponse.getRefreshToken());
        response.addCookie(refreshCookie);

        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

}
