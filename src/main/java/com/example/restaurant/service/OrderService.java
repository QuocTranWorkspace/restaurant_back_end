package com.example.restaurant.service;

import com.example.restaurant.model.OrderEntity;
import com.example.restaurant.model.UserEntity;
import com.example.restaurant.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * The type Order service.
 */
@Service
public class OrderService extends BaseService<OrderEntity> {
    private final OrderRepository orderRepository;
    private final UserService userService;

    /**
     * Instantiates a new Order service.
     *
     * @param orderRepository the order repository
     */
    public OrderService(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
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

    /**
     * Find all by code list.
     *
     * @param code the code
     * @return the list
     */
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
        int userId = orderGet.getUser().getId();
        UserEntity user = userService.getById(userId);
        updateIfNotEmpty(orderGet.getCode(), orderUpdate::setCode);
        orderUpdate.setTotalPrice(orderGet.getTotalPrice());
        if (Objects.nonNull(user) && Boolean.TRUE.equals(user.getStatus())) {
            orderUpdate.setUser(user);
        }
        updateIfNotEmpty(orderGet.getCustomerName(), orderUpdate::setCustomerName);
        updateIfNotEmpty(orderGet.getCustomerEmail(), orderUpdate::setCustomerEmail);
        updateIfNotEmpty(orderGet.getCustomerPhone(), orderUpdate::setCustomerPhone);
        updateIfNotEmpty(orderGet.getCustomerAddress(), orderUpdate::setCustomerAddress);
        System.out.println(orderGet.getCustomerAddress());
        System.out.println(orderUpdate.getCustomerAddress());
        orderUpdate.setDeliveryStatus(orderGet.getDeliveryStatus());

        return orderGet;
    }

    /**
     * Search order list.
     *
     * @param userId the user id
     * @return the list
     */
    public List<OrderEntity> searchOrder(String userId) {
        String sql = "SELECT * FROM tbl_order p WHERE 1=1";

        if (!userId.isEmpty()) {
            sql += " and user_id = " + Integer.parseInt(userId);
        }

        return super.getEntitiesByNativeSQL(sql);
    }
}
