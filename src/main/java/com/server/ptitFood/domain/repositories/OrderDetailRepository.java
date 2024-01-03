package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.Order;
import com.server.ptitFood.domain.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findAllByOrder(Order order);

    // Best seller
    @Query(value = "SELECT * FROM db_orderdetail GROUP BY product_id ORDER BY SUM(count) DESC LIMIT 5", nativeQuery = true)
    List<OrderDetail> bestSeller();

}
