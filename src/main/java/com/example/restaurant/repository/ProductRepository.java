package com.example.restaurant.repository;

import org.springframework.stereotype.Repository;

import com.example.restaurant.model.ProductEntity;

@Repository
public interface ProductRepository extends BaseRepository<ProductEntity> {

    ProductEntity findByProductName(String productName);

}