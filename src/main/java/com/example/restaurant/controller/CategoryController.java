package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.model.CategoryEntity;
import com.example.restaurant.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categoryList")
    public ResponseEntity<ResponseDTO> getCategory() {
        List<CategoryEntity> categories = categoryService.findAll();
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", categories));
    }

}
