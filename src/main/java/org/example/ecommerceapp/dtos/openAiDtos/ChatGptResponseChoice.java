package org.example.ecommerceapp.dtos.openAiDtos;

import lombok.Data;

@Data
public class ChatGptResponseChoice {
    private String index;
    private String text;
}
