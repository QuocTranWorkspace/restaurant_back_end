package com.example.Restaurant.repository;

import com.example.Restaurant.model.CategoryEntity;

public interface CategoryRepository extends BaseRepository<CategoryEntity> {
    CategoryEntity findByCategoryName(String categoryName);
}
