package com.odgoods.authservice.domain.auth.controller;

import com.odgoods.authservice.domain.auth.dto.AuthenticationResponse;
import com.odgoods.authservice.domain.auth.dto.LoginRequest;
import com.odgoods.authservice.domain.auth.dto.RegisterRequest;
import com.odgoods.authservice.domain.auth.dto.UserResponse;
import com.odgoods.authservice.domain.auth.service.AuthService;
import com.odgoods.authservice.domain.auth.util.helper.CookieHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth Management", description = "Operations related to authentication")
public class AuthController {

    private final AuthService authService;
    private final CookieHelper cookieHelper;

    public AuthController(AuthService authService, CookieHelper cookieHelper) {
        this.authService = authService;
        this.cookieHelper = cookieHelper;
    }


    @Operation(summary = "Health check for Auth service", description = "Verifies if the auth service is operational.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auth service is running",
                    content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.status(HttpStatus.OK).body("auth service working");
    }


    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account in the system with provided details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })

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



    @Operation(summary = "User login", description = "Authenticates a user and returns access and refresh tokens.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
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



    @Operation(
            summary = "Get current user info",
            description = "Returns the authenticated user's details.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User data retrieved",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        UserResponse userResponse = authService.me();

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }



    @Operation(
            summary = "Refresh JWT access token",
            description = "Generates a new access token using the refresh token stored in a cookie.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Refresh token is missing or invalid")
    })
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
