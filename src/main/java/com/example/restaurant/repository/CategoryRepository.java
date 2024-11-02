package com.example.restaurant.repository;

import com.example.restaurant.model.CategoryEntity;
import org.springframework.stereotype.Repository;

/**
 * The interface Category repository.
 */
@Repository
public interface CategoryRepository extends BaseRepository<CategoryEntity> {
    /**
     * Find by category name category entity.
     *
     * @param categoryName the category name
     * @return the category entity
     */
    CategoryEntity findByCategoryName(String categoryName);
}