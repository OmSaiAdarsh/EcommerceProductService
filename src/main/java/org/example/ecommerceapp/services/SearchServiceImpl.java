package org.example.ecommerceapp.services;

import org.example.ecommerceapp.models.Product;
import org.example.ecommerceapp.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Service
public class SearchServiceImpl implements SearchService {
    ProductRepository productRepository;

    public SearchServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> search(String name, int pageNo, int pageSize, String sortParam) {
        Sort sort = Sort.by(sortParam).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // convert the things to their UTF
        String encodedSearchTerm = UriUtils.encode(name, StandardCharsets.UTF_8);
        System.out.println(encodedSearchTerm+"  "+ name);
        return productRepository.findByNameContaining(encodedSearchTerm, pageable);
    }

}
