package com.example.Restaurant.service;

import org.springframework.stereotype.Service;

import com.example.Restaurant.model.OrderEntity;
import com.example.Restaurant.repository.OrderRepository;

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
