package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.Order;
import com.server.ptitFood.domain.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    List<OrderDetail> findAllByOrder(Order order);
}
