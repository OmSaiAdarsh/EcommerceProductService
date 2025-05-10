package org.example.ecommerceapp.controllers;

import org.example.ecommerceapp.dtos.OrderItemResponseDto;
import org.example.ecommerceapp.dtos.OrderRequestDto;
import org.example.ecommerceapp.dtos.OrderResponseDto;
import org.example.ecommerceapp.exceptions.GenericExceptionHandlerInThisProject;
import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.models.Order;
import org.example.ecommerceapp.services.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    public OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/")
    public OrderResponseDto createOrder(@RequestHeader("email") String email,
                                        @RequestHeader("token") String token, @RequestBody OrderRequestDto orderRequestDto) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject {
        //System.out.println(orderRequestDto.getEmailId()+" " +orderRequestDto.getIdempotencyKey());
        Order order = orderService.createOrder( token, email,
                orderRequestDto.getEmailId(),
                orderRequestDto.getIdempotencyKey(),
                orderRequestDto.getProducts()
        );
        if (order == null) {
            throw  new ProductNotFoundException("Make sure the Items are sent Correct"); // Actually we should create an exception that should be unique for Order, because of tie constraint going with this approach;
        }
        OrderResponseDto orderResponseDto = OrderResponseDto.from(order);
        return orderResponseDto;
    }
    @PutMapping ("/{orderId}")
    public OrderResponseDto updateOrder(@RequestHeader("email") String email,
                                            @RequestHeader("token") String token, @PathVariable("orderId") long orderId,
                                        @RequestBody OrderRequestDto orderRequestDto) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject {
        Order order = orderService.updateOrder(token, email, orderId, orderRequestDto);
        if(order == null){
            throw new GenericExceptionHandlerInThisProject("Issue in Payload");
        }
        return OrderResponseDto.from(order);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Boolean> cancelOrder(@RequestHeader("email") String email,
                                      @RequestHeader("token") String token, @PathVariable long orderId) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject{
        boolean isCancelled = orderService.cancelOrder(token, email, orderId);
        if (isCancelled) {
            return ResponseEntity.ok(true);
        }
        return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/user/{emailId}")
    public List<OrderResponseDto> getOrdersForEmailId(@RequestHeader("email") String email,
                               @RequestHeader("token") String token, @PathVariable("emailId") String emailId) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject{
        List<Order> orders = orderService.getOrdersForEmailId(token, email, emailId);
        List<OrderResponseDto> dtos = new ArrayList<>();
        for (Order order : orders){
            dtos.add(OrderResponseDto.from(order));
        }
        return dtos;
    }

    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(@RequestHeader("email") String email,
                                     @RequestHeader("token") String token, @PathVariable("orderId") long orderId) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject{
        Order order = orderService.getOrderById(token,email, orderId );
        if (order == null) {
            throw new GenericExceptionHandlerInThisProject("Invalid Order");
        }
        return OrderResponseDto.from(order);
    }

    // this should be for super users(role) only
    @GetMapping("/{emailId}")
    public List<OrderResponseDto> getAllOrders(@RequestHeader("email") String email,
                          @RequestHeader("token") String token, @PathVariable("emailId") String emailId) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject{

        List<Order> orders = orderService.getAllOrders(token, email, emailId);
        List<OrderResponseDto> dtos = new ArrayList<>();
        for (Order order : orders){
            dtos.add(OrderResponseDto.from(order));
        }
        return dtos;
    }

    @GetMapping("/page/{emailId}")
    public Page<OrderResponseDto> getOrdersForEmailIdPage(@RequestHeader("email") String email,
                                                          @RequestHeader("token") String token, @PathVariable("emailId") String emailId,
                                                          @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) throws ProductNotFoundException, UserNotLoggedInException, GenericExceptionHandlerInThisProject{
        Page<Order> pageOrder = orderService.getPageOrdersForEmailId(token, email, emailId, pageNo, pageSize);
        List<OrderResponseDto> dtos = new ArrayList<>();
        for (Order order : pageOrder.getContent()){
            dtos.add(OrderResponseDto.from(order));
        }
        return new PageImpl<>(dtos, pageOrder.getPageable(), pageOrder.getTotalElements());
        //return null;
    }

}
