package com.odgoods.product.domain.service;

import com.odgoods.product.domain.dto.ProductRequest;
import com.odgoods.product.domain.dto.ProductResponse;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse getProductById(String productId);

}
