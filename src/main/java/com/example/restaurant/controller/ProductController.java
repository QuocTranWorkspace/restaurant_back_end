package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.model.ProductEntity;
import com.example.restaurant.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/productList")
    public ResponseEntity<ResponseDTO> getProductList() {
        List<ProductEntity> productList = productService.findAll();
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", productList));
    }
}
