package org.example.ecommerceapp.services;

import org.example.ecommerceapp.dtos.ErrorDTO;
import org.example.ecommerceapp.dtos.openAiDtos.ChatGptRequestDto;
import org.example.ecommerceapp.dtos.openAiDtos.ChatGptResponseChoice;
import org.example.ecommerceapp.dtos.openAiDtos.ChatGptResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.View;

@Service
public class ChatGptService {


    private RestTemplate restTemplate; // = new RestTemplate();
    @Value("${openai.model}")
    private String OPEN_AI_MODEL;
    @Value("${openai.api.uri}")
    private String OPEN_AI_URL;
    public ChatGptService(@Qualifier("getRestTemplateWithAuthorization") RestTemplate restTemplate, View error) {
        this.restTemplate = restTemplate;
    }

    public ChatGptResponseDto getAIResponse(String prompt){
        ChatGptRequestDto chatGptRequestDto = new ChatGptRequestDto();
        chatGptRequestDto.setPrompt(prompt);
        chatGptRequestDto.setModel(OPEN_AI_MODEL);
        System.out.println(OPEN_AI_MODEL);
        ChatGptResponseDto chatGptResponseDto ;
        try {
            chatGptResponseDto = restTemplate.postForObject(OPEN_AI_URL, chatGptRequestDto, ChatGptResponseDto.class);
        }
        catch (Exception e) {
            //ErrorDTO errorDTO = new ErrorDTO();
            //errorDTO.setMessage(e.getMessage());
            chatGptResponseDto = new ChatGptResponseDto();
            chatGptResponseDto.setId(e.getMessage());
            //return chatGptResponseDto;//.setId(e.getMessage());
        }
        //return

        return chatGptResponseDto;
    }
}
