package com.example.Restaurant.repository;

import org.springframework.stereotype.Repository;

import com.example.Restaurant.model.OrderEntity;

@Repository
public interface OrderRepository extends BaseRepository<OrderEntity> {

    OrderEntity findByCode(String code);

}