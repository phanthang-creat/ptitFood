package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.OTP;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OTP, Integer>, JpaSpecificationExecutor<OTP> {

    static Specification<OTP> findByEmailOrUserName(String email, String userName) {
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

//    CREATE DEFINER=`root`@`localhost` PROCEDURE `select_otp`(in username_in varchar(100), in email_in varchar(100), in otp_in varchar(6))
//    BEGIN
//    select * from db_otp where `username` = username_in and `email` = aes_encrypt(email_in, username_in) and `otp` = otp_in;
//    END ;;
    @Procedure(procedureName = "select_otp")
    Optional<OTP> selectOtp(
            @Param("username_in") String username,
            @Param("email_in") String email,
            @Param("otp_in") String otp
    );


}