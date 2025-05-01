package org.example.ecommerceapp.services;

import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.models.Inventory;

public interface InventoryService
{
    public Inventory getInventoryByProductId(String token, String email, long id) throws ProductNotFoundException, UserNotLoggedInException;
    public Inventory addInventory(String token, String email, long productId, int quantity) throws ProductNotFoundException, UserNotLoggedInException ;
    public Inventory updateInventory(String token, String email, long productId, int quantity) throws ProductNotFoundException, UserNotLoggedInException;
    public boolean deleteInventory(String token, String email, long id) throws ProductNotFoundException, UserNotLoggedInException;
    }
