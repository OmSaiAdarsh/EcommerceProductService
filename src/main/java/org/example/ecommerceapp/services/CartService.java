package org.example.ecommerceapp.services;

import org.example.ecommerceapp.dtos.CartItemResponseDto;
import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.models.Cart;
import org.example.ecommerceapp.models.CartItem;

import java.util.List;

public interface CartService {
    public Cart createCart(String token, String email) throws UserNotLoggedInException;
    //public Cart getCart(String token);
    public boolean deleteCart(String token, String email, long id) throws UserNotLoggedInException;
    public Cart getCart(String token, String email, long id) throws UserNotLoggedInException, ProductNotFoundException;
    public boolean removeCartItem(String token, String email, long cartId, long productId) throws UserNotLoggedInException, ProductNotFoundException;
}
