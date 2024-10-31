package com.example.restaurant.repository;

import com.example.restaurant.model.OrderEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends BaseRepository<OrderEntity> {
    OrderEntity findByCode(String code);
}