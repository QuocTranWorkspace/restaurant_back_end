package com.example.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

/**
 * The type Order entity.
 */
@Setter
@Getter
@Entity
@Table(name = "tbl_order")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "code")
public class OrderEntity extends BaseEntity {
    @Column(name = "code")
    private String code;

    @Column(name = "total_price", precision = 13, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_address")
    private String customerAddress;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "delivery_status")
    private int deliveryStatus;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "order")
    private Set<OrderProductEntity> orderProducts;

    /**
     * Add order product.
     *
     * @param orderProduct the order product
     */
    public void addOrderProduct(OrderProductEntity orderProduct) {
        this.orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
    }

    /**
     * Delete order product.
     *
     * @param orderProduct the order product
     */
    public void deleteOrderProduct(OrderProductEntity orderProduct) {
        this.orderProducts.remove(orderProduct);
        orderProduct.setOrder(null);
    }
}