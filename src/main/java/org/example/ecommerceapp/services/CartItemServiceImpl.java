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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    public UserServiceHelper userServiceHelper;
    public CartRepository cartRepository;
    public InventoryRepository inventoryRepository;
    public ProductRepository productRepository;
    public CartItemRepository cartItemRepository;


    CartItemServiceImpl(UserServiceHelper userServiceHelper, CartRepository cartRepository,
                        InventoryRepository inventoryRepository, ProductRepository productRepository,
                        CartItemRepository cartItemRepository) {
        this.userServiceHelper = userServiceHelper;
        this.cartRepository = cartRepository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;


    }

    public CartItem addCartItem(String token, String email, long cartId, long productId, int quantity) throws UserNotLoggedInException, ProductNotFoundException {
        if(!userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isEmpty()){
            throw new ProductNotFoundException("Cart doesnt exist"); // throw CartNotFoundException.
        }
        Cart cart = optionalCart.get();

        // get products from product repository
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()){
            throw new ProductNotFoundException("Product not found");
        }

        Product product = optionalProduct.get();

        // check the quantity passed as a positive integer.
        if (quantity <= 0){
            throw new ProductNotFoundException("Quantity must be greater than 0");// actually throw quantityShouldBePositiveException.
        }

        // get inventory for product id and check the quantity from the inventory.
        Optional<Inventory> optionalInventory = inventoryRepository.findByProduct(product);
        if (optionalInventory.isEmpty()){
            throw new ProductNotFoundException("Product not found in Inventory");// throw Inventory not there for product.
        }
        Inventory inventory = optionalInventory.get();

        //check quantity
        if (inventory.getQuantity() < quantity){
            throw new ProductNotFoundException("Quantity is not sufficient in inventory. Available Quantity: " + inventory.getQuantity());
        }

        // find that whether we have that product in the cart given, if yes, update the quantity.(provided the cart and the product is available and quantity is also available)

        Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndItem(cart, product);

        //initialise the new cartItem
        CartItem cartItem = new CartItem();

        if (optionalCartItem.isPresent()){
            // update the cartItem with the existing data
            cartItem = optionalCartItem.get();
            cartItem.setQuantity(quantity+cartItem.getQuantity());

        }
        else {
            //CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setItem(product);
            cartItem.setQuantity(quantity);
        }
        cartItemRepository.save(cartItem);
        return cartItem;

    }

    public List<CartItem> getAllCartItems(long cartId) throws ProductNotFoundException {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isEmpty()){
            throw new ProductNotFoundException("Cart not found");
        }
        Cart cart = optionalCart.get();

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        return cartItems;
    }

    public CartItem getCartItem(long carItemId) throws ProductNotFoundException {
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(carItemId);
        if (optionalCartItem.isEmpty()){
            throw new ProductNotFoundException("Cart item not found");
        }
        return optionalCartItem.get();
    }

    public CartItem getCartItemFromCartAndProduct(long cartId, long productId) throws ProductNotFoundException {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isEmpty()){
            throw new ProductNotFoundException("Cart not found");
        }
        Cart cart = optionalCart.get();
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()){
            throw new ProductNotFoundException("Product not found");
        }
        Product product = optionalProduct.get();
        Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndItem(cart, product);
        if (optionalCartItem.isEmpty()){
            throw new ProductNotFoundException("Product not found in Cart");
        }
        CartItem cartItem = optionalCartItem.get();
        return cartItem;
    }

    public boolean deleteCartItem(long cartItemId) throws ProductNotFoundException {
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
        if (optionalCartItem.isEmpty()) {
            throw new ProductNotFoundException("Cart item not found");
        }
        CartItem cartItem = optionalCartItem.get();
        cartItemRepository.delete(cartItem);
        return true; //ResponseEntity.ok(true);
    }

}
