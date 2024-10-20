package com.example.restaurant.service;

import org.springframework.stereotype.Service;

import com.example.restaurant.model.ProductEntity;
import com.example.restaurant.repository.ProductRepository;

@Service
public class ProductService extends BaseService<ProductEntity> {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected Class<ProductEntity> clazz() {
        return ProductEntity.class;
    }

    public ProductEntity findByProductName(String productName) {
        return productRepository.findByProductName(productName);
    }

}
