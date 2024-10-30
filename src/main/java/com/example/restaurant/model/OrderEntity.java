package com.example.restaurant.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "tbl_order")
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

    public void addOrderProduct(OrderProductEntity orderProduct) {
        this.orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
    }

    public void deleteOrderProduct(OrderProductEntity orderProduct) {
        this.orderProducts.remove(orderProduct);
        orderProduct.setOrder(null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Set<OrderProductEntity> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(Set<OrderProductEntity> orderProducts) {
        this.orderProducts = orderProducts;
    }

}