package org.example.ecommerceapp.controllers;

import org.example.ecommerceapp.dtos.ProductResponseDTO;
import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.models.Category;
import org.example.ecommerceapp.models.Product;
import org.example.ecommerceapp.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@SpringBootTest
public class ProductControllerTest {
    // Sri Krishna
    @Autowired
    ProductController productController;

    @Qualifier("productDBService")
    @MockitoBean
    ProductService productService;

    public Product getDummyProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Product 1");
        product.setPrice(100.00);

        Category category = new Category();
        category.setId(1L);
        category.setName("Category 1");
        category.setDescription("Category 1");
        product.setCategory(category);

        return product;
    }
    @Test
    public void testGetProductByIdReturnsProductResponseDTO() throws ProductNotFoundException {
        //arrange
        Product dummyProduct = getDummyProduct();
        when(productService.getProductById(anyLong())).thenReturn(dummyProduct);

        //act
        ResponseEntity<ProductResponseDTO> responseEntity = productController.getProductById(1L);
        ProductResponseDTO productResponseDTO = responseEntity.getBody();

        //assert
        //assertEquals(1L,productResponseDTO.getId());
        Assertions.assertEquals(1L, productResponseDTO.getId());
        Assertions.assertEquals("Product 1",productResponseDTO.getName());
        Assertions.assertEquals("Product 1",productResponseDTO.getDescription());
        Assertions.assertEquals(100.00,productResponseDTO.getPrice());
        return ;

    }

    // sri krishna
    @Test
    public void testGetProductByIdReturnsNull() throws ProductNotFoundException {
        Product dummyProduct = getDummyProduct();
        //arange
        when(productService.getProductById(anyLong())).thenReturn(null);
        //act
        ResponseEntity<ProductResponseDTO> responseEntity = productController.getProductById(1L);
        ProductResponseDTO productResponseDTO = responseEntity.getBody();

        //assert
        Assertions.assertNull(productResponseDTO);

    }
}
