package org.example.ecommerceapp.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    @OneToMany(mappedBy = "cart")
    List<CartItem> items;
}
