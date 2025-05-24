package com.odgoods.orderservice.domain.service;
import com.odgoods.orderservice.domain.dto.kafka_message.PushNotificationMessage;
import com.odgoods.orderservice.domain.model.KafkaTopics;
import com.odgoods.orderservice.domain.dto.OrderRequest;
import com.odgoods.orderservice.domain.dto.OrderResponse;
import com.odgoods.orderservice.domain.entity.Order;
import com.odgoods.orderservice.domain.mapper.OrderMapper;
import com.odgoods.orderservice.domain.repository.OrderRepository;
import com.odgoods.orderservice.kafka.OrderKafkaProducer;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private  final OrderMapper orderMapper = new OrderMapper();
    private final OrderKafkaProducer orderKafkaProducer;

    public OrderServiceImpl(OrderRepository orderRepository, OrderKafkaProducer orderKafkaProducer) {
        this.orderRepository = orderRepository;
        this.orderKafkaProducer = orderKafkaProducer;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest, Long userId) {

        Order order = orderMapper.toEntity(orderRequest);
        order.setUserId(userId);
        Order savedOrder = orderRepository.save(order);

        // add the message to the kafka topic: NOTIFICATION-TOPIC
        PushNotificationMessage pushMessage = new PushNotificationMessage(
                KafkaTopics.NOTIFICATION_TOPIC.name(),
                "A new order has been created, try to complete it before the deadline",
                "New Order",
                userId
        );
        orderKafkaProducer.send(KafkaTopics.NOTIFICATION_TOPIC.name(), pushMessage);

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
