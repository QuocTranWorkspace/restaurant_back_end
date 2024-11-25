package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.model.CategoryEntity;
import com.example.restaurant.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Category controller.
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Instantiates a new Category controller.
     *
     * @param categoryService the category service
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Gets category.
     *
     * @return the category
     */
    @GetMapping("/categoryList")
    public ResponseEntity<ResponseDTO> getCategory() {
        List<CategoryEntity> categories = categoryService.findAll();
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", categories));
    }

    /**
     * Gets category.
     *
     * @param id the id
     * @return the category
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseDTO> getCategory(@PathVariable("categoryId") String id) {
        CategoryEntity category = categoryService.getById(Integer.parseInt(id));
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", category));
    }
}
