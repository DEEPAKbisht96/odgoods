package com.odgoods.product.domain.dto;


import com.odgoods.product.domain.model.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private String id;
    private Long userId;
    private String name;
    private String description;
    private String thumbnail;
    private List<String> images;
    private Double priceMin;
    private Double priceMax;
    private boolean isAvailable;
    private List<ProductCategory> categories;
    private List<String> tags;
    private LocalDateTime createdAt;
}
