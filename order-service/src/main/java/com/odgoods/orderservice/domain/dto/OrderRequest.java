package com.odgoods.orderservice.domain.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;


public record OrderRequest(
        @NotNull(message = "Merchant ID is required")
        @Column(name = "merchant_id", nullable = false)
        Long merchantId,

        @NotBlank(message = "Order name is required")
        @Size(max = 100, message = "Order name must not exceed 100 characters")
        String name,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @Size(max = 255, message = "File path must not exceed 255 characters")
        String filePath,

        @DecimalMin(value = "0.0", inclusive = false, message = "Minimum price must be greater than 0")
        Double priceMin,

        @DecimalMin(value = "0.0", inclusive = false, message = "Maximum price must be greater than 0")
        Double priceMax,

        @Future(message = "Deadline must be a future date")
        LocalDateTime deadline
) {

}
