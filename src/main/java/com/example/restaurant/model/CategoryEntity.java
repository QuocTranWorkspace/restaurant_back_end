package com.example.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Category entity.
 */
@Setter
@Getter
@Entity
@Table(name = "tbl_category")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "categoryName")
public class CategoryEntity extends BaseEntity {
    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_description")
    private String categoryDescription;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "category")
    private Set<ProductEntity> products = new HashSet<>();

    /**
     * Add product.
     *
     * @param product the product
     */
    public void addProduct(ProductEntity product) {
        products.add(product);
        product.setCategory(this);
    }

    /**
     * Delete product.
     *
     * @param product the product
     */
    public void deleteProduct(ProductEntity product) {
        products.remove(product);
        product.setCategory(null);
    }

}