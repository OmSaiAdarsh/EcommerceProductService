package org.example.ecommerceapp.dtos.openAiDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGptResponseDto {
    private String id;
    public List<ChatGptResponseChoice> choices;
}
