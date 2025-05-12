package org.example.ecommerceapp.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Value("${OPENAI_API_KEY}")
    private String apiKey;
    @Bean
    @Primary
    public RestTemplate getRestTemplate(CustomResponseErrorHandler customResponseErrorHandler) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(customResponseErrorHandler);
        return restTemplate;
    }

    @Bean
    //@Qualifier("restTemplateAuth")
    public RestTemplate getRestTemplateWithAuthorization(CustomResponseErrorHandler customResponseErrorHandler) {
        RestTemplate restTemplate = new RestTemplate();
        //restTemplate.setErrorHandler(customResponseErrorHandler);
        restTemplate.getInterceptors().add((request, body, execution)->{
            request.getHeaders().add("Authorization", "Bearer "+apiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }

}
