package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findUserByUserName(String username);

    Optional<Customer> findUserByEmail(String email);

    Optional<Customer> findUserByUserNameAndPassword(String username, String password);
}
