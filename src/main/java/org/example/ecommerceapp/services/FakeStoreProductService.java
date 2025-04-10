package org.example.ecommerceapp.services;

import org.example.ecommerceapp.configs.RestTemplateConfig;
import org.example.ecommerceapp.dtos.FakeStoreRequestDTO;
import org.example.ecommerceapp.dtos.FakeStoreResponseDTO;
import org.example.ecommerceapp.exceptions.ProductNotCreatedException;
import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.models.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService{
    public RestTemplate restTemplate;
    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProductById(long id) throws ProductNotFoundException {
        // create a product initially as a practise
//        Product product = new Product();
//        product.setId(id);
//        product.setName("Product Name");
//        return product;

        // Get the product from FakeStore Api and send to controllers
        FakeStoreResponseDTO fakeStoreResponseDTO = restTemplate.getForObject("https://fakestoreapi.com/products/"+id,FakeStoreResponseDTO.class);
        if (fakeStoreResponseDTO == null){
            throw new ProductNotFoundException("Product not found");
        }
        return fakeStoreResponseDTO.toProduct();
        //return null;
    }

    @Override
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        FakeStoreResponseDTO[] fakeStoreResponseDTOS = restTemplate.getForObject("https://fakestoreapi.com/products",FakeStoreResponseDTO[].class);
        for (FakeStoreResponseDTO fakeStoreResponseDTO : fakeStoreResponseDTOS) {
            products.add(fakeStoreResponseDTO.toProduct());
        }
        return products;
    }

    @Override
    public Product addProduct ( String name, String description, double price, String imageUrl, String category) throws ProductNotCreatedException {
        FakeStoreRequestDTO fakeStoreRequestDTO = new FakeStoreRequestDTO();
        fakeStoreRequestDTO.title = name;
        fakeStoreRequestDTO.description = description;
        fakeStoreRequestDTO.price = price;
        fakeStoreRequestDTO.imageUrl = imageUrl;
        fakeStoreRequestDTO.category = category;
        FakeStoreResponseDTO responseDTO  = restTemplate.postForObject("https://fakestoreapi.com/products",fakeStoreRequestDTO,FakeStoreResponseDTO.class);
        if (responseDTO == null){
            throw new ProductNotCreatedException("Product not created");
        }
        return responseDTO.toProduct();
    }
}
