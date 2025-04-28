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



/*


One cart can have only one user
One cart can have multiple products.
Should create an Inventory for the products
add address to the user class.
Each product can be in different quantities
Once an order is placed from cart, the cart item should be placed as ordered in the status and should not be there in cart from next time.
we can directly place order from products, however these should first store it in cart and then do transaction over there.
If they miss out on purchase, we can keep track their interests in cart and send an email.
We can explicitly create a count to see cart is full or not
once an order is placed,
    cart will be added on buy now = kafka
    On filling details like address, on confirm, an order is created with some idempotency as well
    now this orderid is used to track the order,
        These can be async as well may be using a kafka.
    once payment is complete, => Payment Integration can be done, email integration
    mark products in cart as ordered, or decrease the quantity if less than cart count is bought.
    decrease the inventory.
    send an email over the network => async tasks.


    I guess good to go is
    1. Inventory class
    2. Cart with userId, products, status, count[products can be dic of product , count]
    3. support the complete cart order payment
    4. support each product buy payment
    5. a single order id can have multiple transaction_ids where each transaction refers to each product linked to order.
    6. post order take the integration of the stripe payment and after successfull pay, ensure the webhook is called and updates the inventory, email etc.
    7. for each order there should be an unique idempotency key
    8. for each transaction corresponding to cart, since we make order transactional, the transactions will create if order successfylly creates, so order should have a idempotency key.

    Additional include ai bot to elaborate the description about the product, gen ai


 */