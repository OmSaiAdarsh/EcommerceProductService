package org.example.ecommerceapp.controllers;

import org.example.ecommerceapp.dtos.CartItemDto;
import org.example.ecommerceapp.dtos.CartItemResponseDto;
import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.models.CartItem;
import org.example.ecommerceapp.services.CartItemService;
import org.example.ecommerceapp.services.CartService;
import org.springframework.web.bind.annotation.*;

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
}
