package org.example.ecommerceapp.services;

import org.example.ecommerceapp.exceptions.ProductNotCreatedException;
import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.models.BaseModel;
import org.example.ecommerceapp.models.Product;

import java.util.List;

public interface ProductService  {
//    public Product getProductById(long id) throws ProductNotFoundException;
public Product getProductById(long id, String token, String email) throws ProductNotFoundException, UserNotLoggedInException;
    public List<Product> getProducts();
    public Product addProduct( String name, String description, double price, String imageUrl,String category) throws ProductNotCreatedException;

}
