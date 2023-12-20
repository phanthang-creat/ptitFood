package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.Customer;
import com.server.ptitFood.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    @Query(value = "CALL insert_order(:customer_id, :fullname, :phone, :money, :codeDiscount, :address, :status)", nativeQuery = true)
    int insertOrder(
            @Param("customer_id") Integer customerId,
            @Param("fullname") String fullname,
            @Param("phone") String phone,
            @Param("money") Integer money,
            @Param("codeDiscount") Integer codeDiscount,
            @Param("address") String address,
            @Param("status") Integer status
    );

    Integer findOrderById(Integer id);

    Order findById(Integer id);

    List<Order> findAllByUser(Customer user);

    @Procedure(procedureName = "select_order_decryption_by_id")
    Order selectOrderDecryptionById(
            @Param("id") Integer id
    );


}
