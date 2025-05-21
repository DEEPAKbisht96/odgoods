package com.odgoods.authservice.domain.auth.entity;

import java.time.LocalDateTime;
import java.util.List;


import com.odgoods.authservice.domain.auth.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Invalid email")
    @NotBlank(message = "email required")
    @Size(max = 255, message = "email too long")
    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email; 

    @NotBlank(message = "password required")
    @Size(min = 8, message = "password should be more than 8 chars")
    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;

    @Size(max = 100, message = "name too long")
    @Column(name = "first_name", length = 100)
    private String firstName;

    @Size(max = 100, message = "name too long")
    @Column(name = "last_name", length = 100)
    private String lastName;


    @Column(name = "profile_url", columnDefinition = "TEXT")
    private String profileUrl;

    @Builder.Default
    @NotNull
    @Column(name = "is_email_verified", nullable = false)
    private Boolean isEmailVerified = false;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "role", nullable = false, length = 50)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
