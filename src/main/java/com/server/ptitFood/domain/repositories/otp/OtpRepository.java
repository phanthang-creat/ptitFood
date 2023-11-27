package com.server.ptitFood.domain.repositories.otp;

import com.server.ptitFood.domain.entities.OTP;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OTP, Integer>, JpaSpecificationExecutor<OTP> {

    static Specification<OTP> findByEmailOrUserName(String email, String userName) {

        System.out.println("findByEmailOrUserName");
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(
                    criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("email"), email),
                            criteriaBuilder.equal(root.get("userName"), userName)
                    ),
                    criteriaBuilder.greaterThan(root.get("created"), new Date(System.currentTimeMillis() - 3 * 60 * 1000))
            );
        };
    }

    Optional<OTP> findOTPByEmail(String email);

    Optional<OTP> findOTPByUserName(String userName);

    Optional<OTP> findOTPByEmailOrUserName(String email, String userName);

}