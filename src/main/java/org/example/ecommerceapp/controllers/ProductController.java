package org.example.ecommerceapp.controllers;

import org.example.ecommerceapp.dtos.ProductRequestDTO;
import org.example.ecommerceapp.dtos.ProductResponseDTO;
import org.example.ecommerceapp.exceptions.ProductNotCreatedException;
import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.exceptions.UserNotLoggedInException;
import org.example.ecommerceapp.models.Product;
import org.example.ecommerceapp.services.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/products")
public class ProductController {
    public ProductService productService;
    public ProductController(@Qualifier("productDBService") ProductService productService) {
        this.productService = productService;
    }

//    @GetMapping("/{id}")
//    public ProductResponseDTO getProductById(@PathVariable long id) throws ProductNotFoundException {
//
//        Product product = productService.getProductById(id);
//        ProductResponseDTO productResponseDTO = ProductResponseDTO.from(product);
//
//        return productResponseDTO;
//
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable long id,
                                                            @RequestHeader("token") String token,
                                                             @RequestHeader("email") String email)
            throws ProductNotFoundException, UserNotLoggedInException {

        Product product = productService.getProductById(id, token, email);
        ProductResponseDTO productResponseDTO = ProductResponseDTO.from(product);
        ResponseEntity<ProductResponseDTO> responseEntity = new ResponseEntity<>(productResponseDTO, HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping("/")
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productService.getProducts();
        List<ProductResponseDTO> productResponseDTOS = products.stream().map(ProductResponseDTO::from).collect(Collectors.toList()); //product=> ProductResponseDTO::from);
//        List<ProductResponseDTO> productResponseDTOS = new ArrayList<>();
//        for (Product product : products) {
//            ProductResponseDTO productResponseDTO = ProductResponseDTO.from(product);
//            productResponseDTOS.add(productResponseDTO);
//        }
        return productResponseDTOS;
    }

    @PostMapping("/")
    public ProductResponseDTO addProduct(@RequestBody ProductRequestDTO productRequestDTO) throws ProductNotCreatedException {
        Product product = productService.addProduct(
                productRequestDTO.getName(),
                productRequestDTO.getDescription(),
                productRequestDTO.getPrice(),
                productRequestDTO.getImageUrl(),
                productRequestDTO.getCategory()
        );
        System.out.println(product.getName());
        return ProductResponseDTO.from(product);
    }
}
