package com.example.Restaurant.repository;

import org.springframework.stereotype.Repository;

import com.example.Restaurant.model.ProductEntity;

@Repository
public interface ProductRepository extends BaseRepository<ProductEntity> {

    ProductEntity findByProductName(String productName);

}