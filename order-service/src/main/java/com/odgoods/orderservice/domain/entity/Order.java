package com.odgoods.orderservice.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotBlank(message = "Order name is required")
    @Size(max = 100, message = "Order name must not exceed 100 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Size(max = 255, message = "File path must not exceed 255 characters")
    private String filePath;

    @DecimalMin(value = "0.0", inclusive = false, message = "Minimum price must be greater than 0")
    private Double priceMin;

    @DecimalMin(value = "0.0", inclusive = false, message = "Maximum price must be greater than 0")
    private Double priceMax;

    @Future(message = "Deadline must be a future date")
    private LocalDateTime deadline;

    @PastOrPresent(message = "Created time cannot be in the future")
    private LocalDateTime createdAt;

    @PastOrPresent(message = "Updated time cannot be in the future")
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
