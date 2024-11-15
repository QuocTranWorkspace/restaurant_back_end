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
@Entity
@Table(name = "tbl_order")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "code")
public class OrderEntity extends BaseEntity {
    @Setter
    @Getter
    @Column(name = "code")
    private String code;

    @Setter
    @Getter
    @Column(name = "total_price", precision = 13, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Setter
    @Getter
    @Column(name = "customer_name")
    private String customerName;

    @Setter
    @Getter
    @Column(name = "customer_address")
    private String customerAddress;

    @Getter
    @Setter
    @Column(name = "customer_phone")
    private String customerPhone;

    @Setter
    @Getter
    @Column(name = "customer_email")
    private String customerEmail;

    @Setter
    @Getter
    @Column(name = "user_id")
    private int userId;

    @Setter
    @Getter
    @Column(name = "delivery_status")
    private int deliveryStatus;

    @Setter
    @Getter
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