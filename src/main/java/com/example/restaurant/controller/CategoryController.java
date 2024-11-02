package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.model.CategoryEntity;
import com.example.restaurant.model.OrderEntity;
import com.example.restaurant.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseDTO> getCategory(@PathVariable("categoryId") String id) {
        CategoryEntity category = categoryService.getById(Integer.parseInt(id));
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", category));
    }

    @PostMapping("/{categoryId}")
    public ResponseEntity<ResponseDTO> updateCategory(@PathVariable("categoryId") String id, @RequestBody CategoryEntity category) {
        CategoryEntity categorySave = categoryService.getById(Integer.parseInt(id));
        if (Objects.nonNull(categorySave) && !Objects.isNull(category)) {
            categorySave.setCategoryName(category.getCategoryName());
            categorySave.setCategoryDescription(category.getCategoryDescription());
            categorySave.setUpdatedDate(new Date());
            categoryService.saveOrUpdate(categorySave);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "update ok", categorySave));
    }

    @PostMapping("/addCategory")
    public ResponseEntity<ResponseDTO> createCategory(@RequestBody CategoryEntity category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        if (Objects.nonNull(category)) {
            categoryEntity.setCategoryName(category.getCategoryName());
            categoryEntity.setCategoryDescription(category.getCategoryDescription());
            categoryService.saveOrUpdate(categoryEntity);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "update ok", categoryEntity));
    }

    @PostMapping("/deleteCategory/{categoryId}")
    public ResponseEntity<ResponseDTO> deleteOrder(@PathVariable("categoryId") String id) {
        CategoryEntity category = categoryService.getById(Integer.parseInt(id));
        category.setStatus(false);
        categoryService.saveOrUpdate(category);
        return ResponseEntity.ok(new ResponseDTO(200, "deleted", category));
    }
}
