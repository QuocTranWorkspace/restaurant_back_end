package com.example.restaurant.service;

import com.example.restaurant.model.OrderProductEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Order product service.
 */
@Service
public class OrderProductService extends BaseService<OrderProductEntity> {

    @Override
    protected Class<OrderProductEntity> clazz() {
        return OrderProductEntity.class;
    }

    /**
     * Search order products list.
     *
     * @param orderId the order id
     * @return the list
     */
    public List<OrderProductEntity> searchOrderProducts(String orderId) {
        String sql = "SELECT * FROM tbl_order_product p WHERE 1=1";

        if (!orderId.isEmpty()) {
            sql += " and order_id = " + Integer.parseInt(orderId);
        }

        return super.getEntitiesByNativeSQL(sql);
    }
}
