package com.odgoods.product.domain.mapper;

import com.odgoods.product.domain.dto.ProductRequest;
import com.odgoods.product.domain.dto.ProductResponse;
import com.odgoods.product.domain.entity.Product;

public class ProductMapper {

    public Product toEntity(ProductRequest productRequest) {

        Product product = new Product();

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setImages(productRequest.getImages());
        product.setAvailable(productRequest.isAvailable());
        product.setCategories(productRequest.getCategories());
        product.setThumbnail(productRequest.getThumbnail());
        product.setTags(productRequest.getTags());
        product.setPriceMin(productRequest.getPriceMin());
        product.setPriceMax(productRequest.getPriceMax());
        product.setAverageRating(productRequest.getAverageRating());
        product.setTotalReviews(productRequest.getTotalReviews());

        return product;
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getUserId(),
                product.getName(),
                product.getDescription(),
                product.getThumbnail(),
                product.getImages(),
                product.getPriceMin(),
                product.getPriceMax(),
                product.isAvailable(),
                product.getCategories(),
                product.getTags(),
                product.getCreatedAt()
        );
    }

}
