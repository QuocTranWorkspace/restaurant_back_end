package com.example.restaurant.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * The type Product entity.
 */
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

    /**
     * Gets avatar.
     *
     * @return the avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Sets avatar.
     *
     * @param avatar the avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * Gets product name.
     *
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets product name.
     *
     * @param productName the product name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets original price.
     *
     * @return the original price
     */
    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    /**
     * Sets original price.
     *
     * @param originalPrice the original price
     */
    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    /**
     * Gets sale price.
     *
     * @return the sale price
     */
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    /**
     * Sets sale price.
     *
     * @param salePrice the sale price
     */
    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    /**
     * Gets product description.
     *
     * @return the product description
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * Sets product description.
     *
     * @param productDescription the product description
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    /**
     * Gets category.
     *
     * @return the category
     */
    public CategoryEntity getCategory() {
        return category;
    }

    /**
     * Sets category.
     *
     * @param category the category
     */
    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}