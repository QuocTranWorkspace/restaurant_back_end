package com.example.restaurant.service;

import com.example.restaurant.model.OrderProductEntity;
import com.example.restaurant.repository.OrderProductRepository;
import org.springframework.stereotype.Service;

/**
 * The type Order product service.
 */
@Service
public class OrderProductService extends BaseService<OrderProductEntity> {
    private final OrderProductRepository orderProductRepository;

    /**
     * Instantiates a new Order product service.
     *
     * @param orderProductRepository the order product repository
     */
    public OrderProductService(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    protected Class<OrderProductEntity> clazz() {
        return OrderProductEntity.class;
    }
}
