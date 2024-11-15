package com.example.restaurant.service;

import com.example.restaurant.model.OrderEntity;
import com.example.restaurant.model.OrderProductEntity;
import com.example.restaurant.repository.OrderProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<OrderProductEntity> searchOrderProducts(String orderId) {
        String sql = "SELECT * FROM tbl_order_product p WHERE 1=1";

        if (!orderId.isEmpty()) {
            sql += " and order_id = " + Integer.parseInt(orderId);
        }

        return super.getEntitiesByNativeSQL(sql);
    }
}
