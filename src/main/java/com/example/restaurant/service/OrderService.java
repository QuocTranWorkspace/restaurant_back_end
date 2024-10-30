package com.example.restaurant.service;

import com.example.restaurant.model.OrderEntity;
import com.example.restaurant.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends BaseService<OrderEntity> {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    protected Class<OrderEntity> clazz() {
        return OrderEntity.class;
    }

    public OrderEntity findByCode(String code) {
        return orderRepository.findByCode(code);
    }

}
