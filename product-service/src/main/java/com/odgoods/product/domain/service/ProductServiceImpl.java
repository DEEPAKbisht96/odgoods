package com.odgoods.product.domain.service;

import com.odgoods.product.domain.dto.ProductRequest;
import com.odgoods.product.domain.dto.ProductResponse;
import com.odgoods.product.domain.entity.Product;
import com.odgoods.product.domain.mapper.ProductMapper;
import com.odgoods.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private  final ProductMapper productMapper = new ProductMapper();

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest, Long userId) {

        Product product = productMapper.toEntity(productRequest, userId);
        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(String productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        return productMapper.toResponse(product);
    }
}
