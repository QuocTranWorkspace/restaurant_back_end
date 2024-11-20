package com.example.restaurant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * The type Product entity.
 */
@Setter
@Getter
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

}