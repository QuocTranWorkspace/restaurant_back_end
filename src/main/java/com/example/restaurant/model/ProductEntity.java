package com.example.restaurant.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_product")
public class ProductEntity extends BaseEntity {

    @Column(name = "avatar", nullable = true)
    private String avatar;

    @Column(name = "product_name", length = 1000, nullable = false)
    private String productName;

    @Column(name = "original_price", precision = 13, scale = 2, nullable = false)
    private BigDecimal originalPrice;

    @Column(name = "sale_price", precision = 13, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "product_description", length = 3000)
    private String productDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}