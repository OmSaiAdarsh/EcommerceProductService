package org.example.ecommerceapp.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int quantity;
    @ManyToOne
    public Cart cart;
    // public String email; redundancy
    @ManyToOne
    public Product item;

    private Date addedAt;
}
