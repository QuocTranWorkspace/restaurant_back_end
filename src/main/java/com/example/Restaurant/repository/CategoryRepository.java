package com.example.restaurant.repository;

import org.springframework.stereotype.Repository;

import com.example.restaurant.model.CategoryEntity;

@Repository
public interface CategoryRepository extends BaseRepository<CategoryEntity> {

    CategoryEntity findByCategoryName(String categoryName);

}