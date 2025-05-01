package org.example.ecommerceapp.services;

import org.example.ecommerceapp.controllers.InventoryController;
import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.helpers.UserServiceHelper;
import org.example.ecommerceapp.models.Inventory;
import org.example.ecommerceapp.models.Product;
import org.example.ecommerceapp.repositories.InventoryRepository;
import org.example.ecommerceapp.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final ProductRepository productRepository;
    InventoryRepository inventoryRepository;
    UserServiceHelper userServiceHelper;
    public InventoryServiceImpl(InventoryRepository inventoryRepository, UserServiceHelper userServiceHelper, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.userServiceHelper = userServiceHelper;
        this.productRepository = productRepository;
    }

    @Override
    public Inventory getInventoryByProductId(String token, String email,long id) throws UserNotLoggedInException, ProductNotFoundException {
        if(!userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }
        return getInventoryByProductIdInternal(id);
    }
    public Inventory getInventoryByProductIdInternal( long id) throws ProductNotFoundException,UserNotLoggedInException {

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }
        Product product = optionalProduct.get();
        Optional<Inventory> optionalInventory = inventoryRepository.findByProduct(product);
        if (optionalInventory.isEmpty()) {
            throw new ProductNotFoundException("Inventory does not exist for this object");
        }
        Inventory inventory = optionalInventory.get();
        return inventory;
    }

    @Override
    public Inventory addInventory(String token, String email, long productId, int quantity) throws ProductNotFoundException, UserNotLoggedInException {
        if(! userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isEmpty()){
            throw new ProductNotFoundException("Product not found");
        }

        Product product = optionalProduct.get();

        // Inventory finding and getting
        Optional<Inventory> optionalInventory = inventoryRepository.findByProduct(product);

        if(optionalInventory.isPresent()){
            throw new ProductNotFoundException("Inventory already exists, kindly use update endpoint");
        }


        // check quantity in the inventory
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(quantity);
        inventoryRepository.save(inventory);

        return inventory;
    }

    @Override
    public Inventory updateInventory(String token, String email, long productId, int quantity) throws ProductNotFoundException, UserNotLoggedInException  {
        if(! userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isEmpty()){
            throw new ProductNotFoundException("Product not found");

        }
        //Product product = optionalProduct.get();
        Inventory inventory = getInventoryByProductIdInternal(productId);
        inventory.setQuantity(quantity+inventory.getQuantity());
        inventoryRepository.save(inventory);
        return inventory;
    }

    @Override
    public boolean deleteInventory(String token, String email, long id) throws ProductNotFoundException, UserNotLoggedInException {
        if(!userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }

        Inventory inventory = getInventoryByProductIdInternal(id);
        inventoryRepository.delete(inventory);
        return true;


    }
}
