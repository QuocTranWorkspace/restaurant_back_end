package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.model.ProductEntity;
import com.example.restaurant.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * The type Product controller.
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Instantiates a new Product controller.
     *
     * @param productService the product service
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
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", productList));
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
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", product));
    }

    /**
     * Update product response entity.
     *
     * @param id      the id
     * @param avatar  the avatar
     * @param product the product
     * @return the response entity
     * @throws JsonProcessingException the json processing exception
     */
    @PostMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> updateProduct(@PathVariable("productId") String id,
                                                     @RequestPart(value = "avatar", required = false) MultipartFile avatar,
                                                     @RequestPart("product") String product) throws JsonProcessingException {
        ProductEntity productUpdate = productService.getById(Integer.parseInt(id));
        ProductEntity productGet = objectMapper.readValue(product, ProductEntity.class);
        ProductEntity productResponse = null;
        if (Objects.nonNull(productUpdate) && !Objects.isNull(product)) {
            productResponse = productService.bindingProductData(productUpdate, productGet);
            productUpdate.setUpdatedDate(new Date());
            productService.updateProduct(productUpdate, avatar);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "update ok", productResponse));
    }

    /**
     * Create product response entity.
     *
     * @param id      the id
     * @param avatar  the avatar
     * @param product the product
     * @return the response entity
     * @throws JsonProcessingException the json processing exception
     */
    @PostMapping(value = "/addOrder", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> createProduct(@PathVariable("productId") String id,
                                                     @RequestPart(value = "avatar", required = false) MultipartFile avatar,
                                                     @RequestPart("product") String product) throws JsonProcessingException {
        ProductEntity productUpdate = productService.getById(Integer.parseInt(id));
        ProductEntity productGet = objectMapper.readValue(product, ProductEntity.class);
        ProductEntity productResponse = null;
        if (Objects.nonNull(productGet)) {
            productResponse = productService.bindingProductData(productUpdate, productGet);
            productService.saveProduct(productUpdate, avatar);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "update ok", productResponse));
    }

    /**
     * Delete order response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @PostMapping("/deleteProduct/{productId}")
    public ResponseEntity<ResponseDTO> deleteOrder(@PathVariable("productId") String id) {
        ProductEntity productUpdate = productService.getById(Integer.parseInt(id));
        productUpdate.setStatus(false);
        productService.saveOrUpdate(productUpdate);
        ProductEntity productResponse = new ProductEntity();
        productService.bindingProductData(productUpdate, productResponse);
        return ResponseEntity.ok(new ResponseDTO(200, "deleted", productResponse));
    }
}
