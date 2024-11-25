package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.model.ProductEntity;
import com.example.restaurant.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Product controller.
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    /**
     * Instantiates a new Product controller.
     *
     * @param productService  the product service
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
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
}
