package org.example.ecommerceapp.repositories;

import jakarta.transaction.Transactional;
import org.example.ecommerceapp.models.Cart;
import org.example.ecommerceapp.models.CartItem;
import org.example.ecommerceapp.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Transactional
    public void deleteByCart(Cart cart);
    public List<CartItem> findByCart(Cart cart);
    public Optional<CartItem> findByCartAndItem(Cart cart, Product item);
    //public Optional<CartItem> deleteByCartAndItem(Cart cart, Product product);
    public void deleteByCartAndItem(Cart cart, Product product);

}
