package org.example.ecommerceapp.controllers;

import org.example.ecommerceapp.dtos.ProductResponseDTO;
import org.example.ecommerceapp.dtos.SearchRequestDTO;
import org.example.ecommerceapp.models.Product;
import org.example.ecommerceapp.services.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController {
    SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public Page<ProductResponseDTO> search(@RequestBody SearchRequestDTO searchRequestDto){
        Page<Product> productPage = searchService.search(
                searchRequestDto.getName(), searchRequestDto.getPageNumber(),searchRequestDto.getPageSize(), searchRequestDto.getSortParam()
        );
        return getProductDtosFromProduct(productPage);

    }

    @GetMapping("/search")
    public Page<ProductResponseDTO> search(@RequestParam String name,
                                           @RequestParam int pageNumber,
                                           @RequestParam int pageSize,
                                           @RequestParam String sortParam){
        Page<Product> productPage = searchService.search(name, pageNumber, pageSize, sortParam);

        return getProductDtosFromProduct(productPage);
    }

    private Page<ProductResponseDTO> getProductDtosFromProduct(Page<Product> productPage) {
        List<ProductResponseDTO> productResponseDTOs = new ArrayList<>();
        for (Product product : productPage.getContent()) {
            productResponseDTOs.add(ProductResponseDTO.from(product));
        }

        return new PageImpl<>(productResponseDTOs,productPage.getPageable(), productPage.getTotalElements());


    }
}
