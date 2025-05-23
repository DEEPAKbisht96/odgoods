package com.odgoods.orderservice.domain.dto;

import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long merchantId,
        String name,
        String description,
        String filePath,
        Double priceMin,
        Double priceMax,
        LocalDateTime deadline,
        LocalDateTime createdAt
) {
}
