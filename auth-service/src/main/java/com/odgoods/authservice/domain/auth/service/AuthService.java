package com.odgoods.authservice.domain.auth.service;


import com.odgoods.authservice.domain.auth.dto.AuthenticationResponse;
import com.odgoods.authservice.domain.auth.dto.LoginRequest;
import com.odgoods.authservice.domain.auth.dto.RegisterRequest;
import com.odgoods.authservice.domain.auth.dto.UserResponse;

public interface AuthService {
    
    AuthenticationResponse register(RegisterRequest registerRequest);

    AuthenticationResponse login(LoginRequest loginRequest);

    UserResponse me();

    AuthenticationResponse refreshToken(String refreshToken);

    UserResponse getUserById(Long id);
}
