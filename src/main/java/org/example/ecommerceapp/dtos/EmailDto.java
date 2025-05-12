package org.example.ecommerceapp.dtos;

import lombok.Data;

@Data
public class EmailDto {
    private String to;
    private String subject;
    private String body;
    private String from;
}

