package org.example.ecommerceapp.dtos;

import lombok.Data;

@Data
public class ValidateTokenDto {

    private String tokenValue;
    private String email;
}
