package com.example.restaurant.dto.cart;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * The type Cart.
 */
@Setter
@Getter
public class Cart {
    private List<CartItem> cartItems;
    private BigDecimal totalPrice;

}
