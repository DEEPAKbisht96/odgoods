package com.odgoods.orderservice.domain.controller;

import auth.AuthResponse;
import com.odgoods.orderservice.domain.dto.OrderRequest;
import com.odgoods.orderservice.domain.dto.OrderResponse;
import com.odgoods.orderservice.domain.service.OrderService;
import com.odgoods.orderservice.exception.StatusBasedException.BadRequestException;
import com.odgoods.orderservice.exception.StatusBasedException.UnauthorizedException;
import com.odgoods.orderservice.exception.StatusBasedException.NotFoundException;
import com.odgoods.orderservice.grpc.AuthServiceGrpcClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final AuthServiceGrpcClient authServiceGrpcClient;

    public OrderController(OrderService orderService, AuthServiceGrpcClient authServiceGrpcClient) {
        this.orderService = orderService;
        this.authServiceGrpcClient = authServiceGrpcClient;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("X-User-Id") String userIdHeader,
            @Validated @RequestBody OrderRequest orderRequest
    ) {
        Long userId = parseId(userIdHeader, "Invalid user ID in header");

        // validate user existence
        AuthResponse userAuth = authServiceGrpcClient.isUserValid(userId);
        if (userAuth == null || !userAuth.getIsValid()) {
            throw new UnauthorizedException("Invalid or unauthorized user");
        }

        // validate merchant ID
        Long merchantId = orderRequest.merchantId();
        if (merchantId == null || merchantId <= 0) {
            throw new BadRequestException("Merchant ID is required");
        }

        AuthResponse merchantAuth = authServiceGrpcClient.isUserValid(merchantId);
        if (merchantAuth == null || !merchantAuth.getIsValid() || !"MERCHANT".equals(merchantAuth.getRole())) {
            throw new BadRequestException("Invalid or unauthorized merchant");
        }

        log.info("Creating order for user: {}, with merchant: {}", userId, merchantId);

        OrderResponse orderResponse = orderService.createOrder(orderRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<OrderResponse> getOrder(
            @RequestHeader("X-User-Id") String userIdHeader,
            @PathVariable("order_id") String orderIdParam
    ) {
        Long userId = parseId(userIdHeader, "Invalid user ID in header");
        Long orderId = parseId(orderIdParam, "Invalid order ID in path");

        OrderResponse orderResponse = orderService.getOrderById(orderId, userId);
        return ResponseEntity.ok(orderResponse);
    }

    // utility for safe ID parsing with exception wrapping
    private Long parseId(String raw, String errorMessage) {
        try {
            if (raw == null || raw.isBlank()) {
                throw new BadRequestException(errorMessage);
            }
            return Long.parseLong(raw);
        } catch (NumberFormatException ex) {
            throw new BadRequestException(errorMessage);
        }
    }
}
