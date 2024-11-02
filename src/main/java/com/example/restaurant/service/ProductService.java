package com.example.restaurant.service;

import com.example.restaurant.model.ProductEntity;
import com.example.restaurant.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class ProductService extends BaseService<ProductEntity> {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
        if (image == null) {
            return true;
        }
        return !Objects.requireNonNull(image.getOriginalFilename()).isEmpty();
    }

    private String getUniqueUploadFileName(String fileName) {
        String[] splitFileName = fileName.split("\\.");
        return splitFileName[0] + System.currentTimeMillis() + "." + splitFileName[1];
    }

    @SneakyThrows
    @Transactional
    public void saveProduct(ProductEntity product, MultipartFile productAvatar) {
        if (productAvatar != null && !productAvatar.isEmpty()) {

            String fileName = getUniqueUploadFileName(Objects.requireNonNull(productAvatar.getOriginalFilename()));
            String pathToAvatar = "D:/CAGL/CodeForMoney/project_restaurant/upload/product/avatar/" + fileName;
            productAvatar.transferTo(new File(pathToAvatar));
            product.setAvatar("product/avatar/" + fileName);
        }
        super.saveOrUpdate(product);
    }

    @SneakyThrows
    @Transactional
    public void updateProduct(ProductEntity p, MultipartFile productAvatar) {

        if (productAvatar != null) {
            ProductEntity product = super.getById(p.getId());

            if (isEmptyUploadFile(productAvatar)) {
                Path filePath = Paths.get("D:/CAGL/CodeForMoney/project_restaurant/upload/" + product.getAvatar());
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }

                String fileName = getUniqueUploadFileName(Objects.requireNonNull(productAvatar.getOriginalFilename()));
                productAvatar.transferTo(new File("D:/CAGL/CodeForMoney/project_restaurant/upload/product/avatar/" + fileName));
                p.setAvatar("product/avatar/" + fileName);
            } else {
                p.setAvatar(product.getAvatar());
            }
        }

        super.saveOrUpdate(p);
    }
}
