package org.example.ecommerceapp.services;

import org.example.ecommerceapp.dtos.FakeStoreResponseDTO;
import org.example.ecommerceapp.exceptions.ProductNotCreatedException;
import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.models.Category;
import org.example.ecommerceapp.models.Product;
import org.example.ecommerceapp.repositories.CategoryRepository;
import org.example.ecommerceapp.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceTest {
    ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
    ProductService productService = new ProductDBService(productRepository,categoryRepository);
    RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    ProductService fakeProductService = new FakeStoreProductService(restTemplate);

    public Optional<Product> getOptionalProduct(){
        Product product = new Product();
        product.setId(1L);
        product.setPrice(100.0);
        product.setDescription("description");
        product.setName("name");
        product.setCategory(new Category());
        return Optional.of(product);
    }
    @Test
    public void testGetProductByIdReturnsProduct() throws ProductNotFoundException{
        //arrange
        Optional<Product> optionalProduct = getOptionalProduct();
        when(productRepository.findById(anyLong())).thenReturn(optionalProduct);

        // act
        Product product = productService.getProductById(1L);

        //assert
        Assertions.assertEquals(1L, product.getId());
        Assertions.assertEquals("description", product.getDescription());
        Assertions.assertEquals("name", product.getName());
        Assertions.assertEquals(100.0, product.getPrice());
        return;
    }

    @Test
    public void testGetProductByIdThrowsProductNotFoundException() throws ProductNotFoundException{
        //arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        //act
        //Product product = productService.getProductById(1L);

        //assert
        Assertions.assertThrows(ProductNotFoundException.class,()->productService.getProductById(2L));
    }

    @Test
    public void testFakeStoreAddProductReturnsProduct() throws ProductNotCreatedException{
        //arrange
        // testing the fakeStore addProduct method
        FakeStoreResponseDTO fakeStoreResponseDTO = new FakeStoreResponseDTO();
        fakeStoreResponseDTO.setId(1L);
        fakeStoreResponseDTO.setDescription("description");
        fakeStoreResponseDTO.setTitle("title");
        fakeStoreResponseDTO.setImage("image");
        fakeStoreResponseDTO.setCategory("category");
        fakeStoreResponseDTO.setPrice("100.0");

        when(restTemplate.postForObject(
                eq("https://fakestoreapi.com/products"),
                any(),
                eq(FakeStoreResponseDTO.class)
        )).thenReturn(fakeStoreResponseDTO);

        //act
        Product product = fakeProductService.addProduct("title","description", 100.0,"image","category");

        //assert
        Assertions.assertEquals(1L,fakeStoreResponseDTO.getId());
        Assertions.assertEquals("description",fakeStoreResponseDTO.getDescription());
        Assertions.assertEquals("title",fakeStoreResponseDTO.getTitle());
        Assertions.assertEquals("100.0",fakeStoreResponseDTO.getPrice());
        Assertions.assertEquals("image",fakeStoreResponseDTO.getImage());
        Assertions.assertEquals("category",fakeStoreResponseDTO.getCategory());
        return ;
    }

    @Test
    public void testFakeStoreAddProductthrowsProductNotFoundException() throws ProductNotFoundException{
        //arrange
        when(restTemplate.postForObject(
                eq("fakestore/url"),
                any(),
                eq(FakeStoreResponseDTO.class)
        )).thenReturn(null);

        // act

        //assert
        Assertions.assertThrows(ProductNotCreatedException.class,()->fakeProductService.addProduct("name","description",100.0,"image","category"));

        return;
    }
}
