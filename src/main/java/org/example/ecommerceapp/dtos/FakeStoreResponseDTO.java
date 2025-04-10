package org.example.ecommerceapp.dtos;

import lombok.Data;
import org.example.ecommerceapp.models.Category;
import org.example.ecommerceapp.models.Product;

@Data
public class FakeStoreResponseDTO {
    public long id;
    public String title;
    public String description;
    public String image;
    public String price;
    public String category;

    public Product toProduct(){
        if (this == null){
            return null;
        }
        Product product = new Product();
        product.setId(id);
        product.setName(title);
        product.setDescription(description);
        product.setPrice(Float.parseFloat(price));
        product.setImageUrl(image);
        Category category1 = new Category();
        category1.setName(category);
        product.setCategory(category1);
        return product;
    }
}
