package org.example.ecommerceapp.dtos.openAiDtos;

import lombok.Data;

import java.util.List;

@Data
public class ChatGptResponseDto {
    private String id;
    public List<ChatGptResponseChoice> choices;
}
