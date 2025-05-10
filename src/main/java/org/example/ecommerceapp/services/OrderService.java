package org.example.ecommerceapp.services;

import org.example.ecommerceapp.dtos.OrderRequestDto;
import org.example.ecommerceapp.exceptions.GenericExceptionHandlerInThisProject;
import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.models.Order;
import org.example.ecommerceapp.models.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface OrderService {
    public Order createOrder(String token, String email,
                             String EmailID,
                             UUID IdempotencyKey,
                             Map<Long, Integer> products
            ) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject;

    public Order updateOrder(String token , String email, long orderId, OrderRequestDto orderRequestDto) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject ;
    public boolean cancelOrder(String token, String email, long orderId) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject;
    public List<Order> getOrdersForEmailId(String token, String email, String emailId) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject;
    public Order getOrderById(String token, String email, long orderId) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject;
    public List<Order> getAllOrders(String token, String email, String emailId) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject;
    public Page<Order> getPageOrdersForEmailId(String token, String email,
                                               String emailId, int pageNo,
                                               int pageSize) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject ;

    }
