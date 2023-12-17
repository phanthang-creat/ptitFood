package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findAdminByUserName(String userName);

    Admin findAdminByUserNameAndPassword(String userName, String password);

}
