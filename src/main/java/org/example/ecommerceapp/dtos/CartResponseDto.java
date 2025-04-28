package org.example.ecommerceapp.dtos;

import lombok.Data;

@Data
public class CartResponseDto {
    private long cartId;
    public String email;
}
