package org.example.ecommerceapp.helpers;

import org.example.ecommerceapp.dtos.ValidateTokenDto;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceHelper {
    RestTemplate restTemplate ;
    public UserServiceHelper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public  boolean validateToken(String token, String email) {
        String url = "http://localhost:8091/validateToken";
        System.out.println(token+" "+email);
        ValidateTokenDto validateTokenDto = new ValidateTokenDto();
        validateTokenDto.setTokenValue(token);
        validateTokenDto.setEmail(email);
        System.out.println(validateTokenDto.getTokenValue()+" "+validateTokenDto.getEmail());
        ResponseEntity<Boolean> response = restTemplate.exchange(
            url,
                HttpMethod.POST,
                new HttpEntity<>(validateTokenDto),
                Boolean.class
        );
//        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
//            return false;
//        }
        if (response.getStatusCode() == HttpStatus.OK && Boolean.TRUE.equals(response.getBody()) ) {
            System.out.println((response.getBody()));
            return true;
        }
        else
        {
            return false;
        }
    }

}
