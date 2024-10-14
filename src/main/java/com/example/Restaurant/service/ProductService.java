package com.example.Restaurant.service;

import org.springframework.stereotype.Service;

import com.example.Restaurant.model.ProductEntity;
import com.example.Restaurant.repository.ProductRepository;

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
