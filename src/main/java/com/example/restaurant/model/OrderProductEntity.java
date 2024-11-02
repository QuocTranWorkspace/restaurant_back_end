package com.example.restaurant.model;

import jakarta.persistence.*;

/**
 * The type Order product entity.
 */
@Entity
@Table(name = "tbl_order_product")
public class OrderProductEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "quantity")
    private int quantity;

    /**
     * Gets order.
     *
     * @return the order
     */
    public OrderEntity getOrder() {
        return order;
    }

    /**
     * Sets order.
     *
     * @param order the order
     */
    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    /**
     * Gets product.
     *
     * @return the product
     */
    public ProductEntity getProduct() {
        return product;
    }

    /**
     * Sets product.
     *
     * @param product the product
     */
    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    /**
     * Gets quantity.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets quantity.
     *
     * @param quantity the quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}