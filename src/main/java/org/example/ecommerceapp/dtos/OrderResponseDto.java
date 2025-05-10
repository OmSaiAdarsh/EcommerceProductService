package org.example.ecommerceapp.dtos;

import lombok.Data;
import org.example.ecommerceapp.models.Order;
import org.example.ecommerceapp.models.OrderItem;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//import static java.util.stream.Nodes.collect;


@Data
public class OrderResponseDto {
    private long orderId;
    private String email;
    private List<OrderItemResponseDto> orderItems;
    private String orderStatus;
    private double totalAmount;
    private Timestamp createdAt;

    public static OrderResponseDto from(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.createdAt = order.getCreatedAt();
        dto.email = order.getEmail();
        dto.orderId = order.getId();
        dto.orderStatus = order.getOrderStatus().toString();
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderItems(order.getOrderItems()
                .stream()
                .map(OrderItemResponseDto::from)
                .collect(Collectors.toList()));
        return dto;
    }



}
