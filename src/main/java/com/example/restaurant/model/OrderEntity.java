package com.example.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

/**
 * The type Order entity.
 */
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

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets total price.
     *
     * @return the total price
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets total price.
     *
     * @param totalPrice the total price
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Gets customer name.
     *
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets customer name.
     *
     * @param customerName the customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets customer address.
     *
     * @return the customer address
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * Sets customer address.
     *
     * @param customerAddress the customer address
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * Gets customer email.
     *
     * @return the customer email
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * Sets customer email.
     *
     * @param customerEmail the customer email
     */
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    /**
     * Gets customer phone.
     *
     * @return the customer phone
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * Sets customer phone.
     *
     * @param customerPhone the customer phone
     */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    /**
     * Gets order products.
     *
     * @return the order products
     */
    public Set<OrderProductEntity> getOrderProducts() {
        return orderProducts;
    }

    /**
     * Sets order products.
     *
     * @param orderProducts the order products
     */
    public void setOrderProducts(Set<OrderProductEntity> orderProducts) {
        this.orderProducts = orderProducts;
    }
}