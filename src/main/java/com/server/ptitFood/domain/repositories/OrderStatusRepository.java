package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    OrderStatus getOrderStatusById(Integer id);
}
