package org.example.ecommerceapp.repositories;

import org.example.ecommerceapp.models.Inventory;
import org.example.ecommerceapp.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProduct(Product product);
}
