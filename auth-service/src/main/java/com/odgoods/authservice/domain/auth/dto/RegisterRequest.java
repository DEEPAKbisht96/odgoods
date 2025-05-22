package com.odgoods.authservice.domain.auth.dto;

import com.odgoods.authservice.domain.auth.model.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

public record RegisterRequest(

        @Email(message = "Invalid email")
        @NotBlank(message = "Email cannot be blank")
        @Size(max = 255, message = "Email too long")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password,

        @Size(max = 100, message = "First name too long")
        String firstName,

        @Size(max = 100, message = "Last name too long")
        String lastName,

        @Enumerated(EnumType.STRING)
        @NotNull(message = "Role must be provided")
        Role role,

        @URL(message = "Invalid profile URL")
        String profileUrl

) {}
