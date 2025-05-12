package org.example.ecommerceapp.services;

import org.example.ecommerceapp.dtos.ErrorDTO;
import org.example.ecommerceapp.dtos.openAiDtos.ChatGptRequestDto;
import org.example.ecommerceapp.dtos.openAiDtos.ChatGptResponseChoice;
import org.example.ecommerceapp.dtos.openAiDtos.ChatGptResponseDto;
import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.helpers.UserServiceHelper;
import org.example.ecommerceapp.models.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.View;

import java.util.List;

@Service
public class ChatGptService {


    private final UserServiceHelper userServiceHelper;
    private final ProductDBService productDBService;
    private RestTemplate restTemplate; // = new RestTemplate();
    @Value("${openai.model}")
    private String OPEN_AI_MODEL;
    @Value("${openai.api.uri}")
    private String OPEN_AI_URL;
    public ChatGptService(@Qualifier("getRestTemplateWithAuthorization") RestTemplate restTemplate, View error, UserServiceHelper userServiceHelper, ProductDBService productDBService) {
        this.restTemplate = restTemplate;
        this.userServiceHelper = userServiceHelper;
        this.productDBService = productDBService;
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
            chatGptResponseDto.setChoices(List.of(new ChatGptResponseChoice("1", e.getMessage())));
            return chatGptResponseDto;//.setId(e.getMessage());
        }
        //return

        return chatGptResponseDto;
    }


    public String getAIProductDescription(long id, String token, String email) throws UserNotLoggedInException, ProductNotFoundException {
//        if(userServiceHelper.validateToken(token, email)){
//            throw new UserNotLoggedInException();
//        }
        Product product = productDBService.getProductById(id, token, email);

        String prompt = "You are a product descriptor, You are given this product, review and describe it based on price, description, name and category"+
            " The name of the product is " + product.getName() + ", description of project is " + product.getDescription()+", category is " + product.getCategory().getName()
                + " Price is " + product.getPrice() + ". Also recommend who can be the best users of this based on your knowledge.";
        String response = getAIResponse(prompt).getChoices().get(0).getText();
        System.out.println(prompt);
        return response;
    }
}
