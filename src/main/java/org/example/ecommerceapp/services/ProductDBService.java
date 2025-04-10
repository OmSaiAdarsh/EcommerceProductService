package org.example.ecommerceapp.services;

import org.example.ecommerceapp.exceptions.ProductNotCreatedException;
import org.example.ecommerceapp.exceptions.ProductNotFoundException;
import org.example.ecommerceapp.models.Category;
import org.example.ecommerceapp.models.Product;
import org.example.ecommerceapp.repositories.CategoryRepository;
import org.example.ecommerceapp.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("productDBService")
public class ProductDBService implements ProductService{
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    public ProductDBService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public Product getProductById(long id) throws ProductNotFoundException {
        Optional<Product> optionalProduct  = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }
        Product product = optionalProduct.get();
        return product;
    }

    @Override
    public List<Product> getProducts() {
        List<Product> products = productRepository.findAll();

        return products;//  List.of();
    }

    @Override
    public Product addProduct(String name, String description, double price, String imageUrl, String category) throws ProductNotCreatedException {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(imageUrl);
        Category category1 = getCategory(category);
        product.setCategory(category1);
        Product responseProduct = productRepository.save(product);
        return responseProduct;
    }

    public Category getCategory(String category) {
        Optional<Category> optionalCategory = categoryRepository.findByName(category);

        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        }
        Category category1 = new Category();
        category1.setName(category);
        Category category2 = categoryRepository.save(category1);
        return category2;
    }
}
