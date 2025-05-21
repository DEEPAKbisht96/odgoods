package com.odgoods.authservice.domain.auth.dto;

import com.odgoods.authservice.domain.auth.model.Role;

import java.time.LocalDateTime;

public record UserResponse(

        Long id,
        String email,
        String firstName,
        String lastName,
        String profileUrl,
        Role role,
        LocalDateTime createdAt) {
}
