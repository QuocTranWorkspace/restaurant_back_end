package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.model.ProductEntity;
import com.example.restaurant.service.ProductService;
import com.example.restaurant.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Product controller.
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    private final StorageService storageService;

    /**
     * Instantiates a new Product controller.
     *
     * @param productService  the product service
     * @param storageService  the storage service
     */
    public ProductController(ProductService productService, StorageService storageService) {
        this.productService = productService;
        this.storageService = storageService;
    }

    /**
     * Gets product list.
     *
     * @return the product list
     */
    @GetMapping("/productList")
    public ResponseEntity<ResponseDTO> getProductList() {
        List<ProductEntity> productList = productService.findAll();
        return ResponseEntity.ok(new ResponseDTO(200, "get ok1", productList));
    }

    /**
     * Gets product.
     *
     * @param id the id
     * @return the product
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ResponseDTO> getProduct(@PathVariable("productId") String id) {
        ProductEntity product = productService.getById(Integer.parseInt(id));
        return ResponseEntity.ok(new ResponseDTO(200, "get ok2", product));
    }

    /**
     * Gets filtered product list.
     *
     * @param category the category
     * @return the filtered product list
     */
    @GetMapping("/productList/{categoryName}")
    public ResponseEntity<ResponseDTO> getFilteredProductList(@PathVariable("categoryName") String category) {
        List<ProductEntity> productList = productService.searchProduct(category);
        return ResponseEntity.ok(new ResponseDTO(200, "get ok3", productList));
    }

    /**
     * Gets product image URL.
     *
     * @param id the product id
     * @return the product image URL
     */
    @GetMapping("/{productId}/image")
    public ResponseEntity<Map<String, String>> getProductImageUrl(@PathVariable("productId") String id) {
        ProductEntity product = productService.getById(Integer.parseInt(id));

        if (product == null || product.getAvatar() == null || product.getAvatar().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> response = new HashMap<>();
        response.put("url", storageService.getFileUrl(product.getAvatar()));

        return ResponseEntity.ok(response);
    }
}