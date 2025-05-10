package org.example.ecommerceapp.dtos;

import lombok.Data;
import org.example.ecommerceapp.models.Order;
import org.example.ecommerceapp.models.OrderItem;

@Data
public class OrderItemResponseDto {
    private long orderItemId;
    private String orderItemName;
    private int quantity;
    private double price;

    public static OrderItemResponseDto from(OrderItem orderitem) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setOrderItemId(orderitem.getId());
        dto.setOrderItemName(orderitem.getProduct().getName());
        dto.setQuantity(orderitem.getQuantity());
        dto.setPrice(orderitem.getProduct().getPrice());
        return dto;
    }
}
