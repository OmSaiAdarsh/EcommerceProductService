package org.example.ecommerceapp.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CartResponseDto {
    private long cartId;
    public String email;
    public List<CartItemResponseDto> cartItems;
}
