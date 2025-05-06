package org.example.ecommerceapp.services;

import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.models.CartItem;

import java.util.List;

public interface CartItemService {
    public CartItem addCartItem(String token, String email, long cartId, long productId, int quantity) throws UserNotLoggedInException, ProductNotFoundException;

    public List<CartItem> getAllCartItems(long cartId) throws ProductNotFoundException;

    public CartItem getCartItem(long cartItemId) throws ProductNotFoundException;

    public CartItem getCartItemFromCartAndProduct(long cartId, long productId) throws ProductNotFoundException;

    public boolean deleteCartItem(long cartItemId) throws ProductNotFoundException ;
    }
