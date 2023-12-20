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
    Optional<Customer> findUserByUserName(String username);

    Optional<Customer> findUserByUserNameAndStatus(String username, Integer status);

    Customer findByIdAndStatus(Integer id, Integer status);

    Optional<Customer> findUserByEmail(String email);

    Optional<Customer> findUserByUserNameAndPassword(String username, String password);

    @Procedure(procedureName = "insert_customer")
    void insertCustomerWithOtp(
            @Param("fullname") String fullname,
            @Param("username") String username,
            @Param("passwd") String passwd,
            @Param("email") String email
    );

    @Procedure(procedureName = "select_customer_decryption")
    List<Customer> selectCustomerDecryption();

    @Procedure(procedureName = "select_customer_by_email_or_email")
    Optional<Customer> selectCustomerByEmailOrUsername(
            @Param("username_in") String username,
            @Param("email_in") String email
    );

    Page<Customer> findByUsernameContaining(String username, Pageable pageable);

    @Procedure(procedureName = "select_customer_otp_decryption")
    Any selectCustomerOtpDecryption(
            @Param("email_in") String email
    );

    @Modifying
    @Query(value = "update db_customer set status = ? where username = ?", nativeQuery = true)
    void updateStatus(@Param("status") int status, @Param("username") String username);

    @Transactional
    @Procedure(procedureName = "select_customer_decryption_by_username")
    Customer selectCustomerDecryptionByUsername(
            @Param("username_in") String username
    );
}
