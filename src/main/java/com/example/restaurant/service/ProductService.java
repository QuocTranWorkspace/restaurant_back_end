package com.example.restaurant.service;

import com.example.restaurant.model.CategoryEntity;
import com.example.restaurant.model.ProductEntity;
import com.example.restaurant.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Product service.
 */
@Service
public class ProductService extends BaseService<ProductEntity> {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final String filePath;

    /**
     * Instantiates a new Product service.
     *
     * @param productRepository the product repository
     * @param categoryService   the category service
     * @param filePath          the file path
     */
    public ProductService(ProductRepository productRepository, CategoryService categoryService, @Value("${file.path}")String filePath) {
        this.productRepository = productRepository;
        this.categoryService =  categoryService;
        this.filePath = filePath;
    }

    @Override
    protected Class<ProductEntity> clazz() {
        return ProductEntity.class;
    }

    /**
     * Find by product name product entity.
     *
     * @param productName the product name
     * @return the product entity
     */
    public ProductEntity findByProductName(String productName) {
        return productRepository.findByProductName(productName);
    }

    /**
     * Binding product data product entity.
     *
     * @param productUpdate the product update
     * @param productGet    the product get
     * @return the product entity
     */
    public ProductEntity bindingProductData(ProductEntity productUpdate, ProductEntity productGet) {
        updateIfNotEmpty(productGet.getProductName(), productUpdate::setProductName);
        updateIfNotEmpty(productGet.getProductDescription(), productUpdate::setProductDescription);
        productUpdate.setOriginalPrice(productGet.getOriginalPrice());
        productUpdate.setSalePrice(productGet.getSalePrice());

        return productGet;
    }

    private boolean isEmptyUploadFile(MultipartFile image) {
        if (image == null) {
            return true;
        }
        return !Objects.requireNonNull(image.getOriginalFilename()).isEmpty();
    }

    private String getUniqueUploadFileName(String fileName) {
        String[] splitFileName = fileName.split("\\.");
        return splitFileName[0] + System.currentTimeMillis() + "." + splitFileName[1];
    }

    /**
     * Save product.
     *
     * @param product       the product
     * @param productAvatar the product avatar
     */
    @SneakyThrows
    @Transactional
    public void saveProduct(ProductEntity product, MultipartFile productAvatar) {
        if (productAvatar != null && !productAvatar.isEmpty()) {

            String fileName = getUniqueUploadFileName(Objects.requireNonNull(productAvatar.getOriginalFilename()));
            String pathToAvatar = filePath + fileName;
            productAvatar.transferTo(new File(pathToAvatar));
            product.setAvatar(fileName);
        }
        super.saveOrUpdate(product);
    }

    /**
     * Update product.
     *
     * @param p             the p
     * @param productAvatar the product avatar
     */
    @SneakyThrows
    @Transactional
    public void updateProduct(ProductEntity p, MultipartFile productAvatar) {

        if (productAvatar != null) {
            ProductEntity product = super.getById(p.getId());

            if (isEmptyUploadFile(productAvatar)) {
                Path filePath1 = Paths.get(filePath + product.getAvatar());
                if (Files.exists(filePath1)) {
                    Files.delete(filePath1);
                }

                String fileName = getUniqueUploadFileName(Objects.requireNonNull(productAvatar.getOriginalFilename()));
                productAvatar.transferTo(new File(filePath + fileName));
                p.setAvatar(fileName);
            } else {
                p.setAvatar(product.getAvatar());
            }
        }

        super.saveOrUpdate(p);
    }

    /**
     * Search product list.
     *
     * @param categoryName the category name
     * @return the list
     */
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
}
