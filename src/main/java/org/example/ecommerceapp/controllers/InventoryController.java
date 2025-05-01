package org.example.ecommerceapp.controllers;

import org.example.ecommerceapp.dtos.InventoryRequestDto;
import org.example.ecommerceapp.dtos.InventoryResponseDto;
import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.models.Inventory;
import org.example.ecommerceapp.repositories.InventoryRepository;
import org.example.ecommerceapp.services.InventoryService;
import org.example.ecommerceapp.services.InventoryServiceImpl;
import org.example.ecommerceapp.services.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    public InventoryService inventoryService;
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/")
    public InventoryResponseDto addInventory(@RequestBody InventoryRequestDto inventoryRequestDto,
                                            @RequestHeader("token") String token, @RequestHeader("email") String email)
            throws UserNotLoggedInException, ProductNotFoundException {
        InventoryResponseDto responseDto = new InventoryResponseDto();
        Inventory inventory = inventoryService.addInventory(
                token,
                email,
                inventoryRequestDto.getProductId(),
                inventoryRequestDto.getQuantity());
        responseDto.setInventoryId(inventory.getId());
        responseDto.setProductId(inventory.getProduct().getId());
        responseDto.setQuantity(inventory.getQuantity());


        return responseDto;


    }
    @GetMapping("/{id}")
    public InventoryResponseDto getInventory(@PathVariable long id,
                                             @RequestHeader("token") String token, @RequestHeader("email") String email) throws UserNotLoggedInException, ProductNotFoundException {
        Inventory inventory = inventoryService.getInventoryByProductId(token, email, id);
        InventoryResponseDto responseDto = new InventoryResponseDto();
        responseDto.setInventoryId(inventory.getId());
        responseDto.setProductId(inventory.getProduct().getId());
        responseDto.setQuantity(inventory.getQuantity());
        return responseDto;

    }
}
