package com.odgoods.product.domain.dto;

import com.odgoods.product.domain.model.ProductCategory;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name too long")
    private String name;

    @NotBlank(message = "Merchant ID is required")
    @NotEmpty(message = "Merchant ID cannot be empty")
    private String userId;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description too long")
    private String description;

    @Size(max = 10, message = "Maximum 10 images allowed")
    private List<@NotBlank(message = "Image URL cannot be blank") String> images;

    @NotBlank(message = "Thumbnail is required")
    private String thumbnail;

    @NotEmpty(message = "At least one category is required")
    @Max(message = "max 3 categories allowed", value = 3)
    private List<ProductCategory> categories;

    @Size(max = 15, message = "Too many tags")
    private List<@NotBlank String> tags;

    @NotNull(message = "Lower price is required")
    @PositiveOrZero(message = "Lower price must be non-negative")
    private Double priceMin;

    @NotNull(message = "Upper price is required")
    @Positive(message = "Upper price must be positive")
    private Double priceMax;

    private Double averageRating;

    private Integer totalReviews;

    private boolean isAvailable;
}
