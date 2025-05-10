package org.example.ecommerceapp.dtos;

import lombok.Data;
import org.example.ecommerceapp.models.OrderStatus;
import org.example.ecommerceapp.models.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class OrderRequestDto {
    private UUID idempotencyKey;
    private String emailId;
    //private float totalPrice;
    private Map<Long,Integer> products;
    private OrderStatus status;

}
