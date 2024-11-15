package com.example.restaurant.service;

import com.example.restaurant.model.CategoryEntity;
import com.example.restaurant.model.OrderEntity;
import com.example.restaurant.model.ProductEntity;
import com.example.restaurant.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Order service.
 */
@Service
public class OrderService extends BaseService<OrderEntity> {
    private final OrderRepository orderRepository;

    /**
     * Instantiates a new Order service.
     *
     * @param orderRepository the order repository
     */
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    protected Class<OrderEntity> clazz() {
        return OrderEntity.class;
    }

    /**
     * Find by code order entity.
     *
     * @param code the code
     * @return the order entity
     */
    public OrderEntity findByCode(String code) {
        return orderRepository.findByCode(code);
    }

    public List<OrderEntity> findAllByCode(String code) {
        return orderRepository.findAllByCode(code);
    }

    /**
     * Binding order data order entity.
     *
     * @param orderUpdate the order update
     * @param orderGet    the order get
     * @return the order entity
     */
    public OrderEntity bindingOrderData(OrderEntity orderUpdate, OrderEntity orderGet) {
        updateIfNotEmpty(orderGet.getCode(), orderUpdate::setCode);
        orderUpdate.setTotalPrice(orderGet.getTotalPrice());
        updateIfNotEmpty(orderGet.getCustomerName(), orderUpdate::setCustomerName);
        updateIfNotEmpty(orderGet.getCustomerEmail(), orderUpdate::setCustomerEmail);
        updateIfNotEmpty(orderGet.getCustomerPhone(), orderUpdate::setCustomerPhone);
        updateIfNotEmpty(orderGet.getCustomerAddress(), orderUpdate::setCustomerAddress);
        orderUpdate.setDeliveryStatus(orderGet.getDeliveryStatus());

        return orderGet;
    }

    public List<OrderEntity> searchOrder(String userId) {
        String sql = "SELECT * FROM tbl_order p WHERE 1=1";

        if (!userId.isEmpty()) {
            sql += " and user_id = " + Integer.parseInt(userId);
        }

        return super.getEntitiesByNativeSQL(sql);
    }
}
