package org.example.ecommerceapp.dtos;

import lombok.Data;

@Data
public class CartItemDto {
    private long productId;
    private long cartId;
    private int quantity;
}
