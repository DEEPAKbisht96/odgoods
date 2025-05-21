package com.odgoods.product.domain.controller;


import com.odgoods.product.domain.dto.ProductRequest;
import com.odgoods.product.domain.dto.ProductResponse;
import com.odgoods.product.domain.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/products")
@Validated
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck(@RequestHeader("X-User-Id") String userId) {

        log.info("healthCheck userId: {}", userId);

        return ResponseEntity.status(HttpStatus.CREATED).body("product service working");
    }

    @PostMapping("/")
    public ResponseEntity<ProductResponse> createProduct(@Validated @RequestBody @RequestHeader("X-User-Id") String userId, ProductRequest productRequest) {

        log.info("createProduct userId: {}", userId);
        ProductResponse productResponse = productService.createProduct(productRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("product_id") String productId) {
        ProductResponse productResponse = productService.getProductById(productId);
        return ResponseEntity.ok(productResponse);
    }
}
