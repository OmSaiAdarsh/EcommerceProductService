package org.example.ecommerceapp.dtos;

import lombok.Data;
import org.example.ecommerceapp.models.Product;
@Data
public class ProductResponseDTO {
    long id;
    String name;
    String description;
    double price;

    public static ProductResponseDTO from(Product product) {
        if(product == null)
            return null;
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.id = product.getId();
        dto.name = product.getName();
        dto.description = product.getDescription();
        dto.price = product.getPrice();
        return dto;
    }
}
