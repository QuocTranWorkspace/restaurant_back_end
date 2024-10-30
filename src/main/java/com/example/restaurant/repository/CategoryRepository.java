package com.example.restaurant.repository;

import com.example.restaurant.model.CategoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends BaseRepository<CategoryEntity> {

    CategoryEntity findByCategoryName(String categoryName);

}