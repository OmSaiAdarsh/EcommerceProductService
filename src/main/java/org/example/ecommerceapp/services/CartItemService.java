package org.example.ecommerceapp.services;

import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.models.CartItem;

public interface CartItemService {
    public CartItem addCartItem(String token, String email, long cartId, long productId, int quantity) throws UserNotLoggedInException, ProductNotFoundException;

}
