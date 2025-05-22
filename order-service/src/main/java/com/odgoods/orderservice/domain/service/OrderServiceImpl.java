package com.odgoods.orderservice.domain.service;


import com.odgoods.orderservice.domain.dto.OrderRequest;
import com.odgoods.orderservice.domain.dto.OrderResponse;
import com.odgoods.orderservice.domain.entity.Order;
import com.odgoods.orderservice.domain.mapper.OrderMapper;
import com.odgoods.orderservice.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private  final OrderMapper orderMapper = new OrderMapper();

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest, Long userId) {

        Order order = orderMapper.toEntity(orderRequest);
        order.setUserId(userId);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long orderId, Long userId) {

        Order order = orderRepository.getReferenceById(orderId);

        if(!Objects.equals(order.getUserId(), userId)){
            throw new IllegalArgumentException("Order not found with id: " + orderId);
        }

        return orderMapper.toResponse(order);
    }
}
