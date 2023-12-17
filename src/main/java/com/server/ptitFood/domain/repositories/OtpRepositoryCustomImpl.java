package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.OTP;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OtpRepositoryCustomImpl implements OtpRepositoryCustom {

    EntityManager entityManager;

    @Override
    public List<OTP> findByEmailOrUserName(String email, String userName) {
        return entityManager.createQuery(
                "SELECT otp, created FROM OTP otp WHERE otp.email = :email OR otp.userName = :userName" +
                        " AND otp.created > DATE_SUB(NOW(), INTERVAL 3 MINUTE)" +
                        " AND otp.status = 0"
                        , OTP.class)
                .setParameter("email", email)
                .setParameter("userName", userName)
                .getResultList();
    }
}
