package com.odgoods.authservice.domain.auth.service;

import com.odgoods.authservice.common.exception.StatusBasedException.ConflictException;
import com.odgoods.authservice.common.exception.StatusBasedException.UnauthorizedException;
import com.odgoods.authservice.common.exception.StatusBasedException.NotFoundException;
import com.odgoods.authservice.common.exception.StatusBasedException.BadRequestException;

import com.odgoods.authservice.domain.auth.dto.AuthenticationResponse;
import com.odgoods.authservice.domain.auth.dto.LoginRequest;
import com.odgoods.authservice.domain.auth.dto.RegisterRequest;
import com.odgoods.authservice.domain.auth.dto.UserResponse;
import com.odgoods.authservice.domain.auth.entity.User;
import com.odgoods.authservice.domain.auth.mapper.UserMapper;
import com.odgoods.authservice.domain.auth.model.TokenType;
import com.odgoods.authservice.domain.auth.repository.UserRepository;
import com.odgoods.authservice.domain.auth.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
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

        tokenService.saveToken(refreshToken, TokenType.BEARER, false, false, savedUser);

        return Arrays.asList(accessToken, refreshToken);
    }

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new ConflictException("Email already exists. Please try another email address.");
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
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
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
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }

        var userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        return userMapper.toResponse(user);
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadRequestException("Refresh token is empty");
        }

        if (jwtService.isTokenExpired(refreshToken)) {
            throw new UnauthorizedException("Refresh token is expired");
        }

        boolean isTokenValid = tokenService.findByToken(refreshToken);
        if (!isTokenValid) {
            throw new UnauthorizedException("Refresh token is revoked or invalid");
        }

        String userEmail = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String newAccessToken = jwtService.generateToken(userDetails);

        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    public UserResponse getUserById(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid user ID");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        return userMapper.toResponse(user);
    }
}
