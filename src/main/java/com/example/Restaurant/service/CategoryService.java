package com.example.Restaurant.service;

import org.springframework.stereotype.Service;

import com.example.Restaurant.model.CategoryEntity;
import com.example.Restaurant.repository.CategoryRepository;

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
