package com.example.restaurant.service;

import com.example.restaurant.model.CategoryEntity;
import com.example.restaurant.model.ProductEntity;
import com.example.restaurant.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public ProductEntity bindingProductData(ProductEntity productUpdate, ProductEntity productGet) {
        updateIfNotEmpty(productGet.getProductName(), productUpdate::setProductName);
        updateIfNotEmpty(productGet.getProductDescription(), productUpdate::setProductDescription);
        productUpdate.setOriginalPrice(productGet.getOriginalPrice());
        productUpdate.setSalePrice(productGet.getSalePrice());

        return productGet;
    }

    private boolean isEmptyUploadFile(MultipartFile image) {
        return image == null || image.isEmpty() || Objects.requireNonNull(image.getOriginalFilename()).isEmpty();
    }

    @SneakyThrows
    @Transactional
    public void saveProduct(ProductEntity product, MultipartFile productAvatar) {
        if (productAvatar != null && !productAvatar.isEmpty()) {
            String fileName = storageService.store(productAvatar);
            product.setAvatar(fileName);
        }
        super.saveOrUpdate(product);
    }

    @SneakyThrows
    @Transactional
    public void updateProduct(ProductEntity p, MultipartFile productAvatar) {
        if (productAvatar != null && !productAvatar.isEmpty()) {
            ProductEntity existingProduct = super.getById(p.getId());

            // Delete old avatar if it exists
            if (existingProduct.getAvatar() != null && !existingProduct.getAvatar().isEmpty()) {
                storageService.delete(existingProduct.getAvatar());
            }

            // Store new avatar
            String fileName = storageService.store(productAvatar);
            p.setAvatar(fileName);
        } else {
            // Keep existing avatar
            ProductEntity existingProduct = super.getById(p.getId());
            p.setAvatar(existingProduct.getAvatar());
        }

        super.saveOrUpdate(p);
    }


    public List<ProductEntity> searchProduct(String categoryName) {
        if (!categoryName.isEmpty()) {
            String sql = "SELECT * FROM tbl_product p WHERE 1=1 and status = 1";

            CategoryEntity categoryEntity = categoryService.findByCategoryName(categoryName);

            if (categoryEntity != null && categoryEntity.getId() != 0 && categoryEntity.getId() > 0) {
                sql += " and category_id = " + categoryEntity.getId();
            }

            return super.getEntitiesByNativeSQL(sql);
        }
        else return new ArrayList<>();
    }

    // Add method to get image URL for a product
    public String getProductImageUrl(ProductEntity product) {
        if (product.getAvatar() == null || product.getAvatar().isEmpty()) {
            return null;
        }
        return storageService.getFileUrl(product.getAvatar());
    }
}