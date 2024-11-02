package com.example.restaurant.service;

import com.example.restaurant.dto.user.UserDTO;
import com.example.restaurant.model.OrderEntity;
import com.example.restaurant.model.RoleEntity;
import com.example.restaurant.model.UserEntity;
import com.example.restaurant.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

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

    public OrderEntity bindingOrderData(OrderEntity orderUpdate, OrderEntity orderGet) {
        updateIfNotEmpty(orderGet.getCode(), orderUpdate::setCode);
        orderUpdate.setTotalPrice(orderGet.getTotalPrice());
        updateIfNotEmpty(orderGet.getCustomerName(), orderUpdate::setCustomerName);
        updateIfNotEmpty(orderGet.getCustomerEmail(), orderUpdate::setCustomerEmail);
        updateIfNotEmpty(orderGet.getCustomerPhone(), orderUpdate::setCustomerPhone);
        updateIfNotEmpty(orderGet.getCustomerAddress(), orderUpdate::setCustomerAddress);

        return orderGet;
    }
}
