package org.example.ecommerceapp.controllers;

import org.example.ecommerceapp.dtos.CartItemDto;
import org.example.ecommerceapp.dtos.CartItemResponseDto;
import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.models.Cart;
import org.example.ecommerceapp.models.CartItem;
import org.example.ecommerceapp.services.CartItemService;
import org.example.ecommerceapp.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/carts/{cartId}/items/")
public class CartItemController {
    public CartItemService cartItemService;
    public CartService cartService;

    public CartItemController(CartService cartService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;

    }

    @PostMapping("/")
    public CartItemResponseDto addProductToCart(@PathVariable("cartId") long id, @RequestBody CartItemDto cartItemDto,
                                                @RequestHeader("token") String token,
                                                @RequestHeader("email") String email) throws UserNotLoggedInException, ProductNotFoundException {
        if (id == 0 || cartItemDto == null || token == null || email == null)
            throw new RuntimeException("Send all the details");
        CartItem cartItem = cartItemService.addCartItem(token, email,
                cartItemDto.getCartId(),
                cartItemDto.getProductId(),
                cartItemDto.getQuantity());
        CartItemResponseDto cartItemResponseDto = CartItemResponseDto.from(cartItem);
        return cartItemResponseDto;

    }

    @GetMapping("/")
    public List<CartItemResponseDto> getAllCartItems(@PathVariable("cartId") long id) throws ProductNotFoundException{
        if (id == 0 || cartItemService == null) {
            throw new RuntimeException("Send all the details");
        }
        List<CartItemResponseDto> dtos = new ArrayList<>();
        List<CartItem> cartItems = cartItemService.getAllCartItems(id);
        for (CartItem cartItem : cartItems) {
            dtos.add(CartItemResponseDto.from(cartItem));
        }
        return dtos;
    }

    @GetMapping("/{cartItemId}")
    public CartItemResponseDto getCartItem(@PathVariable("cartId") long id, @PathVariable("cartItemId") long cartItemId) throws ProductNotFoundException{
        if (id == 0 || cartItemService == null) {
            throw new RuntimeException("Send all the details");
        }

        CartItem cartItem = cartItemService.getCartItem(cartItemId);
        return CartItemResponseDto.from(cartItem);

    }

    @GetMapping("/find/{itemId}")
    public CartItemResponseDto getCartItemFromCartAndItem(@PathVariable("cartId") long cartId,
                               @PathVariable("itemId") long itemId) throws ProductNotFoundException{
        if(cartId == 0 || itemId == 0 || cartItemService == null) {
            throw new RuntimeException("Send all the details");
        }
        CartItem cartItem = cartItemService.getCartItemFromCartAndProduct(cartId,itemId);
        return CartItemResponseDto.from(cartItem);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Boolean> deleteCartItem(@PathVariable("cartItemId") long cartItemId) throws ProductNotFoundException{
        if (cartItemId == 0 || cartItemService == null) {
            throw new RuntimeException("Send all the details");
        }
        boolean deleted = cartItemService.deleteCartItem(cartItemId);
        if(!deleted){
            return new ResponseEntity<Boolean>(false, HttpStatus.FORBIDDEN); //.notFound().build();
        }
        return ResponseEntity.ok(deleted);
    }
}