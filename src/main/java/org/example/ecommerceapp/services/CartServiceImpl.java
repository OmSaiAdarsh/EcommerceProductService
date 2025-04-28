package org.example.ecommerceapp.services;

import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.helpers.UserServiceHelper;
import org.example.ecommerceapp.models.Cart;
import org.example.ecommerceapp.repositories.CartRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    public UserServiceHelper userServiceHelper;
    public CartRepository cartRepository;
    public CartServiceImpl(UserServiceHelper userServiceHelper, CartRepository cartRepository) {
        this.userServiceHelper = userServiceHelper;
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart createCart(String token, String email) throws UserNotLoggedInException{
        if(!userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }
        Optional<Cart> optionalCart = cartRepository.findByEmail(email);
        if(optionalCart.isPresent()){
            return optionalCart.get();
            // usually throw that cart has already been created, and message as go to the cart section to find your cart.
        }
        Cart cart = new Cart();
        cart.setEmail(email);
        cartRepository.save(cart);
        return cart;
    }
}
