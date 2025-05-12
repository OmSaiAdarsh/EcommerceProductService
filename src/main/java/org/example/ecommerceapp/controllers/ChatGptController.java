package org.example.ecommerceapp.controllers;

import org.example.ecommerceapp.dtos.openAiDtos.ChatGptResponseDto;
import org.example.ecommerceapp.services.ChatGptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/api")
public class ChatGptController {
    private final ChatGptService chatGptService;
    public ChatGptController(final ChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }
    @GetMapping("/")
    public ChatGptResponseDto getChatGptResponse(@RequestParam String prompt) {
        // Here this is entirely chatgpt controller, for now lets do it here instead of service
        return chatGptService.getAIResponse(prompt);//"Hello World";
    }
}
