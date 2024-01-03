package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.Customer;
import com.server.ptitFood.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findOrderById(Integer id);

    List<Order> findAllByUser(Customer user);

    @Query(value = "SELECT SUM(money) FROM Order")
    Long totalMoney();

    // today count
    @Query(value = "SELECT COUNT(*) FROM Order WHERE orderDate = CURDATE()")
    Long todayCount();

    // today money
    @Query(value = "SELECT SUM(money) FROM Order WHERE orderDate = CURDATE()")
    Long todayMoney();

    // Lấy doanh mỗi tháng trong vòng 7 tháng gần nhất
    @Query(value = "SELECT SUM(money) FROM ptitfood.db_order WHERE order_date BETWEEN DATE_SUB(CURDATE(), INTERVAL 7 MONTH) AND CURDATE() GROUP BY MONTH(order_date)", nativeQuery = true)
    List<Long> getMoneyByMonth();

    //   Get month, year, money by month in 7 month
    @Query(value = "SELECT MONTH(order_date) as month, YEAR(order_date) as year, SUM(money) as money FROM ptitfood.db_order WHERE order_date BETWEEN DATE_SUB(CURDATE(), INTERVAL 7 MONTH) AND CURDATE() GROUP BY MONTH(order_date), YEAR(order_date)", nativeQuery = true)
    List<Object[]> getMoneyByMonth2();
}
