package com.example.restaurant.service;

import org.springframework.stereotype.Service;

import com.example.restaurant.model.OrderEntity;
import com.example.restaurant.repository.OrderRepository;

@Service
public class OrderService extends BaseService<OrderEntity> {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    protected Class<OrderEntity> clazz() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OrderEntity findByCode(String code) {
        return orderRepository.findByCode(code);
    }

}
