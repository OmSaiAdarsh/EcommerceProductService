package org.example.ecommerceapp.dtos;

import lombok.Data;

@Data
public class CartProductDto {
    private int product_id;
    private int cart_id;
    private int quantity;
}
