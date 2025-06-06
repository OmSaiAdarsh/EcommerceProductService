package org.example.ecommerceapp.repositories;

import org.example.ecommerceapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByNameContaining(String name, Pageable pageable);
}
