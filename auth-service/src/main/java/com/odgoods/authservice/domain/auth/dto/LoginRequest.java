package com.odgoods.authservice.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @Email(message = "invalid credentails")
        @NotBlank(message = "email cannot be empty")
        @Size(max = 255, message = "too long email")
        String email,

        @NotBlank(message = "invalid credentails")
        @Size(min = 8, message = "invalid credentails")
        String password
) {

}
