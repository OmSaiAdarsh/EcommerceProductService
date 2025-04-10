package org.example.ecommerceapp.services;

import org.example.ecommerceapp.models.Product;
import org.springframework.data.domain.Page;

public interface SearchService {
    public Page<Product> search(String name, int pageNo, int pageSize, String SortParam);
}
