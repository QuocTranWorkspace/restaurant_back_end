package com.example.restaurant.dto.cart;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Cart item.
 */
@Setter
@Getter
public class CartItem {
    private int productId;
    private int quantity;

}
