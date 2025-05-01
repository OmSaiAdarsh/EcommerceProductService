package org.example.ecommerceapp.dtos;

import lombok.Data;

@Data
public class InventoryResponseDto {
    private long inventoryId;
    private long productId;
    private int quantity;
}
