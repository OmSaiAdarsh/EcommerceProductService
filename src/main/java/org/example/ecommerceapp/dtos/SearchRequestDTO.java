package org.example.ecommerceapp.dtos;

import lombok.Data;

@Data
public class SearchRequestDTO {
    String name;
    int pageNumber;
    int pageSize;
    String SortParam;
}
