package com.odgoods.orderservice.domain.mapper;


import com.odgoods.orderservice.domain.dto.OrderRequest;
import com.odgoods.orderservice.domain.dto.OrderResponse;
import com.odgoods.orderservice.domain.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toEntity(OrderRequest orderRequest){
        Order order = new Order();
        order.setName(orderRequest.name());
        order.setDescription(orderRequest.description());
        order.setDeadline(orderRequest.deadline());
        order.setFilePath(orderRequest.filePath());
        order.setPriceMax(orderRequest.priceMax());
        order.setPriceMin(orderRequest.priceMin());

        return order;
    }

    public OrderResponse toResponse(Order order){
        return new OrderResponse(
                order.getId(),
                order.getName(),
                order.getDescription(),
                order.getFilePath(),
                order.getPriceMin(),
                order.getPriceMax(),
                order.getDeadline(),
                order.getCreatedAt()
        );
    }
}
