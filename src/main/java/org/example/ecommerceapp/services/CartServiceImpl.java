package org.example.ecommerceapp.services;

import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.helpers.UserServiceHelper;
import org.example.ecommerceapp.models.Cart;
import org.example.ecommerceapp.models.CartItem;
import org.example.ecommerceapp.models.Inventory;
import org.example.ecommerceapp.models.Product;
import org.example.ecommerceapp.repositories.CartItemRepository;
import org.example.ecommerceapp.repositories.CartRepository;
import org.example.ecommerceapp.repositories.InventoryRepository;
import org.example.ecommerceapp.repositories.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    public UserServiceHelper userServiceHelper;
    public CartRepository cartRepository;
    public ProductRepository productRepository;
    public InventoryRepository inventoryRepository;

    public CartServiceImpl(UserServiceHelper userServiceHelper, CartRepository cartRepository,
                           CartItemRepository cartItemRepository, ProductRepository productRepository,
                           InventoryRepository inventoryRepository) {
        this.userServiceHelper = userServiceHelper;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;

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
            return false;
            //throw new UserNotLoggedInException(); // actually throw cart not created Exception
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

    public Cart getCart(String token, String email, long id) throws UserNotLoggedInException, ProductNotFoundException{
        if(!userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if (optionalCart.isEmpty()) {
            throw new ProductNotFoundException("Cart doesn't exist");
            // throw new UserNotLoggedInException(); // actually throw the cart not created Exception.
        }
        Cart cart = optionalCart.get();
        // List<CartItem> cartItems = cart.getItems();
        return cart;
    }

//    public CartItem addCartItem(String token, String email, long cartId, long productId, int quantity) throws UserNotLoggedInException, ProductNotFoundException{
//        if(!userServiceHelper.validateToken(token, email)){
//            throw new UserNotLoggedInException();
//        }
//        Optional<Cart> optionalCart = cartRepository.findById(cartId);
//        if (optionalCart.isEmpty()){
//            throw new ProductNotFoundException("Cart doesnt exist"); // throw CartNotFoundException.
//        }
//        Cart cart = optionalCart.get();
//
//        // get products from product repository
//        Optional<Product> optionalProduct = productRepository.findById(productId);
//        if (optionalProduct.isEmpty()){
//            throw new ProductNotFoundException("Product not found");
//        }
//
//        Product product = optionalProduct.get();
//
//        // check the quantity passed as a positive integer.
//        if (quantity <= 0){
//            throw new ProductNotFoundException("Quantity must be greater than 0");// actually throw quantityShouldBePositiveException.
//        }
//
//        // get inventory for product id and check the quantity from the inventory.
//        Optional<Inventory> optionalInventory = inventoryRepository.findByProduct(product);
//        if (optionalInventory.isEmpty()){
//            throw new ProductNotFoundException("Product not found in Inventory");// throw Inventory not there for product.
//        }
//        Inventory inventory = optionalInventory.get();
//
//        //check quantity
//        if (inventory.getQuantity() < quantity){
//            throw new ProductNotFoundException("Quantity is not sufficient in inventory. Available Quantity: " + inventory.getQuantity());
//        }
//
//        // find that whether we have that product in the cart given, if yes, update the quantity.(provided the cart and the product is available and quantity is also available)
//
//        Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndItem(cart, product);
//
//        //initialise the new cartItem
//        CartItem cartItem = new CartItem();
//
//        if (optionalCartItem.isPresent()){
//            // update the cartItem with the existing data
//            cartItem = optionalCartItem.get();
//            cartItem.setQuantity(quantity+cartItem.getQuantity());
//
//        }
//        else {
//            //CartItem cartItem = new CartItem();
//            cartItem.setCart(cart);
//            cartItem.setItem(product);
//            cartItem.setQuantity(quantity);
//        }
//        cartItemRepository.save(cartItem);
//        return cartItem;
//
//    }

    public boolean removeCartItem(String token, String email, long cartId, long productId) throws UserNotLoggedInException, ProductNotFoundException{
        if(!userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }

        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isEmpty()){
            throw new ProductNotFoundException("Cart not found"); // create this = throw new CartNotFoundException()
        }
        Cart cart = optionalCart.get();

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()){
            throw new ProductNotFoundException("Product not found");
        }
        Product product = optionalProduct.get();

        Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndItem(cart, product);

        if (optionalCartItem.isEmpty()){
            throw new ProductNotFoundException("Product not found in cart"); // throw CartItemNotFoundException.
        }

        CartItem cartItem = optionalCartItem.get();
        cartItemRepository.delete(cartItem);
        //cartItemRepository.deleteById(cartItem.getId());
        return true;



    }

}
