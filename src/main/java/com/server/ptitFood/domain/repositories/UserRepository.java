package com.server.ptitFood.domain.repositories;


import com.server.ptitFood.domain.entities.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findUserByUserName(String username);

    Optional<Customer> findUserByEmail(String email);

    Optional<Customer> findUserByUserNameAndPassword(String username, String password);

//    CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_customer_with_otp`(in fullname varchar(100), in username varchar(100), in passwd varchar(100), in email varchar(100), in otp varchar(6))
//    BEGIN
//    insert into db_customer (username, password, email, fullname, trash, status, role, otp) value (username, sha2(passwd, 256), aes_encrypt(email, username), fullname, 0, 0, 3, otp );
//    END ;;
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
}
