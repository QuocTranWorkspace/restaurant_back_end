package com.example.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Category entity.
 */
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

    /**
     * Gets category name.
     *
     * @return the category name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets category name.
     *
     * @param categoryName the category name
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Gets category description.
     *
     * @return the category description
     */
    public String getCategoryDescription() {
        return categoryDescription;
    }

    /**
     * Sets category description.
     *
     * @param categoryDescription the category description
     */
    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    /**
     * Gets products.
     *
     * @return the products
     */
    public Set<ProductEntity> getProducts() {
        return products;
    }

    /**
     * Sets products.
     *
     * @param products the products
     */
    public void setProducts(Set<ProductEntity> products) {
        this.products = products;
    }
}