package org.example.ecommerceapp.dtos;

import lombok.Data;
import org.example.ecommerceapp.models.CartItem;

@Data
public class CartItemResponseDto {
    private long cartItemId;
    private long productId;
    private String itemName;
    private Integer quantity;
    //private int status;

    public static CartItemResponseDto from(CartItem cartItem) {
        CartItemResponseDto dto = new CartItemResponseDto();
        dto.cartItemId = cartItem.getId();
        dto.productId = cartItem.getItem().getId();
        dto.itemName = cartItem.getItem().getName();
        dto.quantity = cartItem.getQuantity();
        return dto;
    }
}
