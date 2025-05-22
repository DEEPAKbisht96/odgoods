package com.odgoods.orderservice.domain.controller;


import auth.AuthResponse;
import com.odgoods.orderservice.domain.dto.OrderRequest;
import com.odgoods.orderservice.domain.dto.OrderResponse;
import com.odgoods.orderservice.domain.service.OrderService;
import com.odgoods.orderservice.grpc.AuthServiceGrpcClient;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
            @RequestHeader("X-User-Id") String userId,
            @Validated @RequestBody OrderRequest orderRequest
    ) throws BadRequestException {

        // check if the user is valid then only create.
        AuthResponse authResponse = authServiceGrpcClient.isUserValid(Long.parseLong(userId));

        if (authResponse == null || !authResponse.getIsValid()) {
            throw new BadRequestException("Invalid user id");
        }

        System.out.println("userId: " + userId);
        System.out.println("orderRequest: " + orderRequest);

        OrderResponse orderResponse = orderService.createOrder(orderRequest, Long.parseLong(userId));

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<OrderResponse> getOrder(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable("order_id") String orderId
    ){

        OrderResponse orderResponse = orderService.getOrderById(Long.parseLong(orderId), Long.parseLong(userId));

        return ResponseEntity.ok(orderResponse);

    }
}
