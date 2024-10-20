package com.example.restaurant.repository;

import org.springframework.stereotype.Repository;

import com.example.restaurant.model.OrderEntity;

@Repository
public interface OrderRepository extends BaseRepository<OrderEntity> {

    OrderEntity findByCode(String code);

}