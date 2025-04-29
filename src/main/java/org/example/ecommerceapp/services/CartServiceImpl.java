package org.example.ecommerceapp.services;

import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.helpers.UserServiceHelper;
import org.example.ecommerceapp.models.Cart;
import org.example.ecommerceapp.models.CartItem;
import org.example.ecommerceapp.repositories.CartItemRepository;
import org.example.ecommerceapp.repositories.CartRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    public UserServiceHelper userServiceHelper;
    public CartRepository cartRepository;
    public CartServiceImpl(UserServiceHelper userServiceHelper, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.userServiceHelper = userServiceHelper;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
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

    public boolean deleteCart(String token, String email, long id) throws UserNotLoggedInException{
        if(!userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if (optionalCart.isEmpty()){
            throw new UserNotLoggedInException(); // actually throw cart not created Exception
        }
        Cart cart = optionalCart.get();

        // delete all the items that are present in the cart
        cartItemRepository.deleteByCart(cart);

        // optionally delete all the cart items in the cart list as well in order to make sure it is updated and able to work on from now
        cart.getItems().clear();
        cartRepository.save(cart);
        //cartRepository.deleteById(id);
        return true;
    }

    public List<CartItem> getCartItems(String token, String email, long id) throws UserNotLoggedInException{
        if(!userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if (optionalCart.isEmpty()) {
            throw new UserNotLoggedInException(); // actually throw the cart not created Exception.
        }
        Cart cart = optionalCart.get();
        List<CartItem> cartItems = cart.getItems();
        return cartItems;
    }
}
