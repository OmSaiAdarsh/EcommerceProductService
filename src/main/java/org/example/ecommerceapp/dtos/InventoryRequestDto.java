package org.example.ecommerceapp.dtos;

import lombok.Data;

@Data
public class InventoryRequestDto {
    private long productId;
    private int quantity;
}
