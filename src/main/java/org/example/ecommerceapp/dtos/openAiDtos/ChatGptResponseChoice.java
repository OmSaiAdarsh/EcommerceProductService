package org.example.ecommerceapp.dtos.openAiDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGptResponseChoice {
    private String index;
    private String text;
}
