package com.example.restaurant.service;

import com.example.restaurant.model.CategoryEntity;
import com.example.restaurant.model.ProductEntity;
import com.example.restaurant.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ProductService extends BaseService<ProductEntity> {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final StorageService storageService;

    public ProductService(ProductRepository productRepository,
                          CategoryService categoryService,
                          StorageService storageService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.storageService = storageService;
    }

    @Override
    protected Class<ProductEntity> clazz() {
        return ProductEntity.class;
    }

    public ProductEntity findByProductName(String productName) {
        return productRepository.findByProductName(productName);
    }

    /**
     * Copy properties from source product to target product
     */
    public ProductEntity bindingProductData(ProductEntity targetProduct, ProductEntity sourceProduct) {
        // Make sure to match the exact property names in your entity
        if (sourceProduct.getProductName() != null) {
            targetProduct.setProductName(sourceProduct.getProductName());
        }
        if (sourceProduct.getProductDescription() != null) {
            targetProduct.setProductDescription(sourceProduct.getProductDescription());
        }
        targetProduct.setOriginalPrice(sourceProduct.getOriginalPrice());
        targetProduct.setSalePrice(sourceProduct.getSalePrice());
        if (sourceProduct.getStatus() != null) {
            targetProduct.setStatus(sourceProduct.getStatus());
        }

        // Only set category if it's provided
        if (sourceProduct.getCategory() != null) {
            targetProduct.setCategory(sourceProduct.getCategory());
        }

        return targetProduct;
    }

    @Override
    public void updateIfNotEmpty(String value, java.util.function.Consumer<String> setter) {
        if (value != null && !value.trim().isEmpty()) {
            setter.accept(value);
        }
    }

    @Transactional
    public void saveProduct(ProductEntity product, MultipartFile productAvatar) {
        try {
            log.info("Saving product: {}", product.getProductName());

            if (productAvatar != null && !productAvatar.isEmpty()) {
                log.info("Processing avatar for product, file name: {}, size: {}",
                        productAvatar.getOriginalFilename(), productAvatar.getSize());

                // Store the file and get the filename
                String fileName = storageService.store(productAvatar);
                product.setAvatar(fileName);

                log.info("Avatar stored successfully with filename: {}", fileName);
            } else {
                log.info("No avatar provided for product");
            }

            super.saveOrUpdate(product);
            log.info("Product saved successfully, ID: {}", product.getId());

        } catch (IOException e) {
            log.error("Failed to save product avatar", e);
            throw new RuntimeException("Failed to upload product image: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void updateProduct(ProductEntity product, MultipartFile productAvatar) {
        try {
            log.info("Updating product with ID: {}", product.getId());

            ProductEntity existingProduct = super.getById(product.getId());
            if (existingProduct == null) {
                log.error("Product not found with ID: {}", product.getId());
                throw new RuntimeException("Product not found with ID: " + product.getId());
            }

            if (productAvatar != null && !productAvatar.isEmpty()) {
                log.info("Processing new avatar for product, file name: {}, size: {}",
                        productAvatar.getOriginalFilename(), productAvatar.getSize());

                // Delete old avatar if it exists
                if (existingProduct.getAvatar() != null && !existingProduct.getAvatar().isEmpty()) {
                    try {
                        log.info("Deleting old avatar: {}", existingProduct.getAvatar());
                        storageService.delete(existingProduct.getAvatar());
                    } catch (IOException e) {
                        log.warn("Failed to delete old avatar: {}", existingProduct.getAvatar(), e);
                        // Continue with the update even if delete fails
                    }
                }

                // Store new avatar
                String fileName = storageService.store(productAvatar);
                product.setAvatar(fileName);
                log.info("New avatar stored successfully with filename: {}", fileName);
            } else {
                // Keep existing avatar
                log.info("No new avatar provided, keeping existing avatar: {}", existingProduct.getAvatar());
                product.setAvatar(existingProduct.getAvatar());
            }

            super.saveOrUpdate(product);
            log.info("Product updated successfully");

        } catch (IOException e) {
            log.error("Failed to update product avatar", e);
            throw new RuntimeException("Failed to upload product image: " + e.getMessage(), e);
        }
    }

    public List<ProductEntity> searchProduct(String categoryName) {
        if (categoryName != null && !categoryName.isEmpty()) {
            String sql = "SELECT * FROM tbl_product p WHERE 1=1 AND p.status = 1";

            CategoryEntity categoryEntity = categoryService.findByCategoryName(categoryName);

            if (categoryEntity != null && categoryEntity.getId() > 0) {
                sql += " AND p.category_id = " + categoryEntity.getId();
            }

            return super.getEntitiesByNativeSQL(sql);
        }
        else return new ArrayList<>();
    }

    // Add method to get image URL for a product
    public String getProductImageUrl(ProductEntity product) {
        if (product == null || product.getAvatar() == null || product.getAvatar().isEmpty()) {
            return null;
        }
        return storageService.getFileUrl(product.getAvatar());
    }
}