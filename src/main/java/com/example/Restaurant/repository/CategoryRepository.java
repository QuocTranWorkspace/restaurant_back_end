package com.example.Restaurant.repository;

import org.springframework.stereotype.Repository;

import com.example.Restaurant.model.CategoryEntity;

@Repository
public interface CategoryRepository extends BaseRepository<CategoryEntity> {

    CategoryEntity findByCategoryName(String categoryName);

}