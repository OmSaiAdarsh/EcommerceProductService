package org.example.ecommerceapp.dtos;

import lombok.Data;

@Data
public class CartProductResponseDto {
    private long cartProductId;
    private int status;
}
