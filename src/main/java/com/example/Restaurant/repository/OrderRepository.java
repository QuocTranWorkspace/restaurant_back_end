package com.example.Restaurant.repository;

import com.example.Restaurant.model.OrderEntity;

public interface OrderRepository extends BaseRepository<OrderEntity> {

    OrderEntity findByCode(String code);

}