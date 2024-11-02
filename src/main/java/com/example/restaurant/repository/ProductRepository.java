package com.example.restaurant.repository;

import com.example.restaurant.model.ProductEntity;
import org.springframework.stereotype.Repository;

/**
 * The interface Product repository.
 */
@Repository
public interface ProductRepository extends BaseRepository<ProductEntity> {
    /**
     * Find by product name product entity.
     *
     * @param productName the product name
     * @return the product entity
     */
    ProductEntity findByProductName(String productName);
}