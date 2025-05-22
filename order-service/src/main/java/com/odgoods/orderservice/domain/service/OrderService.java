package com.odgoods.orderservice.domain.service;


import com.odgoods.orderservice.domain.dto.OrderRequest;
import com.odgoods.orderservice.domain.dto.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(OrderRequest orderRequest, Long userId);

    OrderResponse getOrderById(Long orderId, Long userId);
}
