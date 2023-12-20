package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.Cart;
import com.server.ptitFood.domain.entities.Customer;
import com.server.ptitFood.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByUserAndProduct(Customer user, Product product);

    List<Cart> findByUser(Customer user);

    void deleteAllByUser(Customer user);
}
