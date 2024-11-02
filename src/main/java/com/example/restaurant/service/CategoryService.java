package com.example.restaurant.service;

import com.example.restaurant.model.CategoryEntity;
import com.example.restaurant.repository.CategoryRepository;
import org.springframework.stereotype.Service;

/**
 * The type Category service.
 */
@Service
public class CategoryService extends BaseService<CategoryEntity> {
    private final CategoryRepository categoryRepository;

    /**
     * Instantiates a new Category service.
     *
     * @param categoryRepository the category repository
     */
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    protected Class<CategoryEntity> clazz() {
        return CategoryEntity.class;
    }

    /**
     * Find by category name category entity.
     *
     * @param categoryName the category name
     * @return the category entity
     */
    public CategoryEntity findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }
}
