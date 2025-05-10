package org.example.ecommerceapp.repositories;

import org.aspectj.weaver.ast.Or;
import org.example.ecommerceapp.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    public Optional<Order> findByIdempotencyKey(UUID key);
    public List<Order> findByEmail(String email);
    public Page<Order> findAllByEmail(String email, Pageable pageable);
}
