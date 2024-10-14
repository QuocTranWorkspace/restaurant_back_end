package com.example.Restaurant.repository;

import com.example.Restaurant.model.ProductEntity;

public interface ProductRepository extends BaseRepository<ProductEntity> {

    ProductEntity findByProductName(String productName);

}