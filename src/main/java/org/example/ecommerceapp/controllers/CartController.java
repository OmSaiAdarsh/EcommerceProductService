package org.example.ecommerceapp.controllers;

import org.example.ecommerceapp.dtos.CartProductDto;
import org.example.ecommerceapp.dtos.CartRequestDto;
import org.example.ecommerceapp.dtos.CartResponseDto;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.models.Cart;
import org.example.ecommerceapp.services.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {
    public CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @PostMapping("/")
    public CartResponseDto createCart(@RequestBody CartRequestDto cartRequestDto,
                                      @RequestHeader("token") String token,
                                      @RequestHeader("email") String email) throws UserNotLoggedInException {
        CartResponseDto cartResponseDto = new CartResponseDto();
        Cart cart = cartService.createCart(token, email);
        if (cart == null) {
            throw new RuntimeException();
        }
        cartResponseDto.setCartId(cart.getId());
        cartResponseDto.setEmail(cart.getEmail());
        return cartResponseDto;
    }

    @DeleteMapping("/{id}")
    public void deleteCart(@PathVariable long id){}

    @GetMapping("/")
    public void getCart(@PathVariable long id){}

    @PostMapping("/{id}/products")
    public void addProductToCart(@PathVariable long id, @RequestBody CartProductDto cartProductDto){

    }
    @DeleteMapping("/{id}/products/{productId}")
    public void deleteProductFromCart(@PathVariable("id") long id, @PathVariable("productId") long productId){}

    @PutMapping("/{id}/products/{productId}")
    public void updateProductInCart(@PathVariable long id, @PathVariable long productId){}




}
