package com.odgoods.product.domain.entity;


import com.odgoods.product.domain.model.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String id;

    @Field("user_id")
    private Long userId;

    private String name;

    private String description;

    private List<String> images;

    private String thumbnail;

    private List<ProductCategory> categories;

    private List<String> tags;

    @Field("price_min")
    private Double priceMin;

    @Field("price_max")
    private Double priceMax;

    @Field("is_available")
    private boolean isAvailable;

    @Field("rating")
    private Double averageRating;

    @Field("total_reviews")
    private Integer totalReviews;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;
}
