package org.example.ecommerceapp.services;

import org.example.ecommerceapp.dtos.OrderRequestDto;
import org.example.ecommerceapp.dtos.OrderResponseDto;
import org.example.ecommerceapp.exceptions.GenericExceptionHandlerInThisProject;
import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.helpers.UserServiceHelper;
import org.example.ecommerceapp.models.*;
import org.example.ecommerceapp.repositories.InventoryRepository;
import org.example.ecommerceapp.repositories.OrderItemRepository;
import org.example.ecommerceapp.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderItemRepository orderItemRepository;
    public UserServiceHelper userServiceHelper;
    public ProductService productService;
    public OrderRepository orderRepository;
    public InventoryService inventoryService;

    public OrderServiceImpl(UserServiceHelper userServiceHelper, @Qualifier("productDBService") ProductService productService, OrderRepository orderRepository, InventoryService inventoryService,
                            OrderItemRepository orderItemRepository) {
        this.userServiceHelper = userServiceHelper;
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
        this.orderItemRepository = orderItemRepository;
    }
    @Transactional(rollbackFor = Exception.class) // for entire Exception class, usually Transactional is for unchecked exceptions.
    // specific to few checked exceptions
    //@Transactional(rollbackFor = {GenericExceptionHandlerInThisProject.class, ProductNotFoundException.class})

    @Override
    public Order createOrder(String token, String email, String emailID, UUID idempotencyKey, Map<Long, Integer> products) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject {
        // first validate user
        if (!userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }

        // check the user from auth and the payload is same or not
        if (! email.equals(emailID)){
            throw new GenericExceptionHandlerInThisProject("Invalid access");
        }
        System.out.println(email+" "+emailID);
        // if idempotencyKey is not present, straight away return Missing key on Exception
        if (idempotencyKey == null){
            throw new GenericExceptionHandlerInThisProject("Missing Important unique key");
        }

        //if idempotencyKey is already present in db, return order corresponding to that
        Optional<Order> optionalOrder = orderRepository.findByIdempotencyKey(idempotencyKey);
        if (optionalOrder.isPresent()){
            return optionalOrder.get();
        }

        //check all the products are valid
        Product product;
        double totalAmount = 0;
        // now after clearing all the edge cases, create the order.
        Order order = new Order();
        order.setIdempotencyKey(idempotencyKey);
        order.setEmail(email);
        order.setOrderStatus(OrderStatus.INITIATED);
        order.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        order.setOrderItems(new ArrayList<>());
        order = orderRepository.save(order);
        System.out.println("Order first part completed");
        for (Map.Entry<Long, Integer> mapEntry : products.entrySet()){
            product = productService.getProductById(mapEntry.getKey(), token, email);
            if (product == null){
                throw new ProductNotFoundException("Product not found");
            }
            Inventory inventory = inventoryService.getInventoryByProductId(token, email, product.getId());
            if (mapEntry.getValue() > inventory.getQuantity()) {
                throw new GenericExceptionHandlerInThisProject("Available inventory is : " + inventory.getQuantity() + ", kindly make sure you are buying based on this value.");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(mapEntry.getValue());
            orderItem.setOrder(order);
            orderItem.setUnitPrice(product.getPrice());
            orderItemRepository.save(orderItem);
            inventoryService.updateInventory(token, email, product.getId(), inventory.getQuantity() - mapEntry.getValue());
            totalAmount += product.getPrice() * mapEntry.getValue();
            order.getOrderItems().add(orderItem);
        }
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);
        return order;
    }

    public Order updateOrder(String token , String email, long orderId, OrderRequestDto orderRequestDto) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject {
        if (!userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()){
            throw new GenericExceptionHandlerInThisProject("OrderId not found");
        }
        Order order = optionalOrder.get();

        // check if the valid user is accessing the order
        if (!order.getEmail().equals(email)){
            throw new GenericExceptionHandlerInThisProject("Invalid access");
        }

        // now we have authenticated and authorized for the order

        // check for the fields that need to change, only valid fields have to be changed like order_status,
        // if there is other details like products exist and not matching, email exist and not matching, return errorDto.
        if(orderRequestDto.getEmailId() != null && !order.getEmail().equals( orderRequestDto.getEmailId())){
            throw new GenericExceptionHandlerInThisProject("Bad Request");
        }

        if (orderRequestDto.getProducts() != null){
            List<Long> orderItemIds = order.getOrderItems().stream().map(orderItem->{return orderItem.getProduct().getId();}).toList();
            for (Map.Entry<Long, Integer> mapEntry : orderRequestDto.getProducts().entrySet()){
                if(!orderItemIds.contains(mapEntry.getKey())) {
                    throw new GenericExceptionHandlerInThisProject("Product not found in order, Bad Request");
                }
            }
        }

        if (orderRequestDto.getStatus() != null && orderRequestDto.getStatus().getValue() <= order.getOrderStatus().getValue()){
            throw new GenericExceptionHandlerInThisProject("This state cant be taken at this stage..");
        }

        order.setOrderStatus(orderRequestDto.getStatus());
        orderRepository.save(order);
        return order;
    }

    /**
     * @param token
     * @param email
     * @param orderId
     * @return
     */
    @Override
    public boolean cancelOrder(String token, String email, long orderId) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject{
        Order order = getOrderById(token, email, orderId);

        try {
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }
        catch (Exception e){
            // some exception has happened,
            return false;
        }

        return true;
    }

    /**
     * @param token
     * @param email
     * @param emailId
     * @return
     * @throws ProductNotFoundException
     * @throws UserNotLoggedInException
     * @throws GenericExceptionHandlerInThisProject
     */
    @Override
    public List<Order> getOrdersForEmailId(String token, String email, String emailId) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject {
        if (!userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }

        // check for payload email and header email, check they are equal
        if(emailId == null || emailId.isEmpty() || !email.equals(emailId)){
            throw new GenericExceptionHandlerInThisProject("Invalid access");
        }
        List<Order> orders = orderRepository.findByEmail(emailId);
        return orders;
    }

    /**
     * @param token
     * @param email
     * @param orderId
     * @return
     * @throws ProductNotFoundException
     * @throws UserNotLoggedInException
     * @throws GenericExceptionHandlerInThisProject
     */
    @Override
    public Order getOrderById(String token, String email, long orderId) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject {
        if(!userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()){
            throw new GenericExceptionHandlerInThisProject("OrderId not found");
        }
        Order order = optionalOrder.get();
        if (!order.getEmail().equals(email)){
            throw new GenericExceptionHandlerInThisProject("Invalid access");
        }

        // now order is validated and authorized and authenticated.
        return order;
    }

    /**
     * @param token
     * @param email
     * @param emailId
     * @return
     * @throws ProductNotFoundException
     * @throws UserNotLoggedInException
     * @throws GenericExceptionHandlerInThisProject
     */
    @Override
    public List<Order> getAllOrders(String token, String email, String emailId) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject {
        if(!userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }

        // check if the email is a superUser (currently since we dont have roles, lets keep one email as a super user and then check for it.
        if(! email.equals(emailId) || !email.equals("omsaiadarsh@gmail.com")){
            throw new GenericExceptionHandlerInThisProject("Invalid access");
        }
        List<Order> orders = orderRepository.findByEmail(emailId);
        return orders;
    }

    public Page<Order> getPageOrdersForEmailId(String token, String email,
                                               String emailId, int pageNo,
                                               int pageSize) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject {
        if(!userServiceHelper.validateToken(token, email)){
            throw new UserNotLoggedInException();
        }

        if (!email.equals(emailId)){
            throw new GenericExceptionHandlerInThisProject("Invalid access");
        }
        // valid
        // by default sort descending
        Sort sort  = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Order> pageOrder= orderRepository.findAllByEmail(email, pageable);
        return pageOrder;
    }

}
