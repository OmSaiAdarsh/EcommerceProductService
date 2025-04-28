package org.example.ecommerceapp.services;

import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.models.Cart;

public interface CartService {
    public Cart createCart(String token, String email) throws UserNotLoggedInException;
    //public Cart getCart(String token);
}
