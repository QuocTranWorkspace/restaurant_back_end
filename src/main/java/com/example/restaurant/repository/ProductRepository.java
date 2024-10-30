package com.example.restaurant.repository;

import com.example.restaurant.model.ProductEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseRepository<ProductEntity> {

    ProductEntity findByProductName(String productName);

}