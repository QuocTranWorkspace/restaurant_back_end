package com.example.restaurant.service;

import com.example.restaurant.model.CategoryEntity;
import com.example.restaurant.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends BaseService<CategoryEntity> {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    protected Class<CategoryEntity> clazz() {
        return CategoryEntity.class;
    }

    public CategoryEntity findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

}
