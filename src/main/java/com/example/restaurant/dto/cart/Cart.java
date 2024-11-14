package com.example.restaurant.dto.cart;

import java.math.BigDecimal;
import java.util.List;

/**
 * The type Cart.
 */
public class Cart {
    private List<CartItem> cartItems;
    private BigDecimal totalPrice;

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
