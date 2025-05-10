package org.example.ecommerceapp.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Order order;
    @ManyToOne
    private Product product;
    private int quantity = 0;
    private double unitPrice;
}
