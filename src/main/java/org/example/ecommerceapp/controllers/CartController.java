package org.example.ecommerceapp.controllers;

import org.example.ecommerceapp.dtos.CartItemDto;
import org.example.ecommerceapp.dtos.CartItemResponseDto;
import org.example.ecommerceapp.dtos.CartRequestDto;
import org.example.ecommerceapp.dtos.CartResponseDto;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.models.Cart;
import org.example.ecommerceapp.models.CartItem;
import org.example.ecommerceapp.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<Boolean> deleteCart(@PathVariable long id,
                                              @RequestHeader("token") String token,
                                              @RequestHeader("email") String email) throws UserNotLoggedInException {
        ResponseEntity<Boolean> responseEntity;
        boolean isDeleted = cartService.deleteCart(token, email, id);
        if (isDeleted) {
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/")
    public List<CartItemResponseDto> getCart(@PathVariable long id,
                                             @RequestHeader("token") String token,
                                             @RequestHeader("email") String email) throws UserNotLoggedInException{
        List<CartItem> cartItems = cartService.getCartItems(token, email, id);

        List<CartItemResponseDto> cartItemResponseDtos = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            cartItemResponseDtos.add(CartItemResponseDto.from(cartItem));
        }
        return cartItemResponseDtos;


    }

    @PostMapping("/{id}/products")
    public void addProductToCart(@PathVariable long id, @RequestBody CartItemDto cartItemDto){

    }
    @DeleteMapping("/{id}/products/{productId}")
    public void deleteProductFromCart(@PathVariable("id") long id, @PathVariable("productId") long productId){}

    @PutMapping("/{id}/products/{productId}")
    public void updateProductInCart(@PathVariable long id, @PathVariable long productId){}




}
