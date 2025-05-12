package org.example.ecommerceapp.dtos.openAiDtos;

import lombok.Data;

@Data
public class ChatGptRequestDto {
    private String model;
    private String prompt;
    private int temperature = 1;
    private int max_tokens = 100;

}
