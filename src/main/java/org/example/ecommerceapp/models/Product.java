package org.example.ecommerceapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

@Entity
@Data
public class Product extends BaseModel {
    //jai sri krishna
    // jai sri rama, jai sai baba
    //long id;
    //String name;
    double price;
    String description;
    String imageUrl;
    @ManyToOne
    @JsonIgnore
    public Category category;

//    public Product() {
//    }


    protected boolean canEqual(final Object other) {
        return other instanceof Product;
    }

}
