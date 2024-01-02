package com.server.ptitFood.domain.repositories;


import com.server.ptitFood.domain.entities.Customer;

import org.hibernate.mapping.Any;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findCustomerByUsername(String username);

    Optional<Customer> findUserByUsernameAndStatus(String username, Integer status);

    Customer findByIdAndStatus(Integer id, Integer status);

    Optional<Customer> findUserByEmail(String email);

    Optional<Customer> findUserByUsernameAndPassword(String username, String password);

    Optional<Customer> findCustomerByEmailOrUsername(String email, String username);

    Page<Customer> findByUsernameContaining(String username, Pageable pageable);

    Page<Customer> findByEmailContaining(String email, Pageable pageable);



    @Modifying
    @Query(value = "update db_customer set status = ? where username = ?", nativeQuery = true)
    void updateStatus(@Param("status") int status, @Param("username") String username);

}
