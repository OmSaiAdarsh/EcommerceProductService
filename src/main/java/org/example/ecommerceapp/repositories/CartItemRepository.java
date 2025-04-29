package org.example.ecommerceapp.repositories;

import jakarta.transaction.Transactional;
import org.example.ecommerceapp.models.Cart;
import org.example.ecommerceapp.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Transactional
    public void deleteByCart(Cart cart);
}
