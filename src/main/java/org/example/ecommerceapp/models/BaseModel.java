package org.example.ecommerceapp.models;

import jakarta.persistence.*;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Data
@MappedSuperclass
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;

    public BaseModel() {
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BaseModel;
    }

}
