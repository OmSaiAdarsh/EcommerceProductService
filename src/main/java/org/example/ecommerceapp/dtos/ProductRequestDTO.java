package org.example.ecommerceapp.dtos;

import lombok.Data;

@Data
public class ProductRequestDTO {
    public String name;
    public String description;
    public String category;
    public double price;
    public String imageUrl;
}

