package org.example.ecommerceapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Category extends BaseModel{
    public String description;
    @OneToMany(mappedBy = "category")
    public List<Product> products;
}
