package com.odgoods.authservice.domain.auth.mapper;

import com.odgoods.authservice.domain.auth.dto.RegisterRequest;
import com.odgoods.authservice.domain.auth.dto.UserResponse;
import com.odgoods.authservice.domain.auth.entity.User;
import com.odgoods.authservice.domain.auth.model.Role;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    // convert to the entity understood by the database
    public User toEntity(RegisterRequest dto) {

        User user = new User();

        // Directly set the role from the DTO (with a fallback if null)
        Role role = dto.role() != null ? dto.role() : Role.USER;
        user.setRole(role);

        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setProfileUrl(dto.profileUrl());
        user.setLastName(dto.lastName());
        user.setPassword(dto.password());
        user.setIsEmailVerified(false);

        return user;
    }

    // convert to a response understood by the client
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfileUrl(),
                user.getRole(),
                user.getCreatedAt());
    }

}
