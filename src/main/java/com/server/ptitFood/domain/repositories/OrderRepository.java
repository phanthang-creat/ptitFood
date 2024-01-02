package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.Customer;
import com.server.ptitFood.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findOrderById(Integer id);

    List<Order> findAllByUser(Customer user);
}
