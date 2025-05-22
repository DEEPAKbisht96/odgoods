package com.odgoods.authservice.domain.auth.service;

import com.odgoods.authservice.domain.auth.dto.AuthenticationResponse;
import com.odgoods.authservice.domain.auth.dto.LoginRequest;
import com.odgoods.authservice.domain.auth.dto.RegisterRequest;
import com.odgoods.authservice.domain.auth.dto.UserResponse;
import com.odgoods.authservice.domain.auth.entity.User;
import com.odgoods.authservice.domain.auth.exception.EmailAlreadyExistsException;
import com.odgoods.authservice.domain.auth.mapper.UserMapper;
import com.odgoods.authservice.domain.auth.model.TokenType;
import com.odgoods.authservice.domain.auth.repository.UserRepository;
import com.odgoods.authservice.domain.auth.security.CustomUserDetails;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;

import java.util.*;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           JwtService jwtService, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    private List<String> makeToken(User user, User savedUser) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", user.getId());
        claims.put("first_name", user.getFirstName());
        claims.put("profile", user.getProfileUrl());
        claims.put("role", user.getRole());

        String accessToken = jwtService.generateToken(claims, new CustomUserDetails(savedUser));
        String refreshToken = jwtService.generateRefreshToken(new CustomUserDetails(savedUser));

        // save the token to the database for further validation in future
        tokenService.saveToken(refreshToken, TokenType.BEARER, false, false, savedUser);

        return new ArrayList<>(Arrays.asList(accessToken, refreshToken));
    }

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {

        // check if the user already exists.
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new EmailAlreadyExistsException(null, null);
        }

        User user = userMapper.toEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        List<String> tokens = makeToken(user, savedUser);

        return new AuthenticationResponse(
                tokens.get(0),
                tokens.get(1),
                userMapper.toResponse(savedUser));

    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {

        // check if the user email is present or not
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        // validate the password

        System.out.println("MATCHING THE PASSWORDS request : " + loginRequest.password());
        System.out.println("MATCHING THE PASSWORDS db : " + user.getPassword());

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("invalid credentails");
        }

        List<String> tokens = makeToken(user, user);

        return AuthenticationResponse.builder()
                .accessToken(tokens.get(0))
                .refreshToken(tokens.get(1))
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    public UserResponse me() {
        // Get the authentication object from security context
        org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        // Check if user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadCredentialsException("User not authenticated");
        }

        // Get the user details from authentication principal
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Fetch the user from repository to get latest data
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return userMapper.toResponse(user);
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        // Validate the refresh token structure first
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadCredentialsException("Refresh token is empty");
        }

        // Validate the refresh token signature and expiration
        if (jwtService.isTokenExpired(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        // Check if token exists in database and is not revoked or expired
        boolean isTokenValid = tokenService.findByToken(refreshToken);

        if (!isTokenValid) {
            throw new BadCredentialsException("Refresh token is expired or revoked");
        }

        // Extract user details from token
        String userEmail = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Create user details for token generation
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // Generate new tokens
        String newAccessToken = jwtService.generateToken(userDetails);

        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    public UserResponse getUserById(Long id) {

        // validating the id before a query.
        if (id == null || id <= 0) {
            throw new BadCredentialsException("Invalid user");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        return userMapper.toResponse(user);
    }

}
