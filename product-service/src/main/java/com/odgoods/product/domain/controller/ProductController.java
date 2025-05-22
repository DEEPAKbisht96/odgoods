package com.odgoods.product.domain.controller;


import auth.AuthResponse;
import com.odgoods.product.domain.dto.ProductRequest;
import com.odgoods.product.domain.dto.ProductResponse;
import com.odgoods.product.domain.service.ProductService;
import com.odgoods.product.grpc.AuthServiceGrpcClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
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
    private final AuthServiceGrpcClient authServiceGrpcClient;

    public ProductController(ProductService productService, AuthServiceGrpcClient authServiceGrpcClient) {
        this.productService = productService;
        this.authServiceGrpcClient = authServiceGrpcClient;
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck(@RequestHeader("X-User-Id") String userId) {

        log.info("healthCheck userId: {}", userId);

        return ResponseEntity.status(HttpStatus.CREATED).body("product service working");
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestHeader("X-User-Id") String userId,
            @Validated @RequestBody ProductRequest productRequest
    ) throws BadRequestException {
        log.info("createProduct userId: {}", userId);
        Long id = Long.parseLong(userId);

        AuthResponse authResponse = authServiceGrpcClient.isUserValid(id);

        if (authResponse == null || !authResponse.getIsValid()) {
            throw new BadRequestException("Invalid user id");
        }

        ProductResponse productResponse = productService.createProduct(productRequest, id);

        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }


    @GetMapping("/{product_id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("product_id") String productId) {
        ProductResponse productResponse = productService.getProductById(productId);
        return ResponseEntity.ok(productResponse);
    }
}
