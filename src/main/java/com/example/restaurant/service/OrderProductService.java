package com.example.restaurant.service;

import org.springframework.stereotype.Service;

import com.example.restaurant.model.OrderProductEntity;
import com.example.restaurant.repository.OrderProductRepository;

@Service
public class OrderProductService extends BaseService<OrderProductEntity> {

    private final OrderProductRepository orderProductRepository;

    public OrderProductService(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    protected Class<OrderProductEntity> clazz() {
        return OrderProductEntity.class;
    }

}
