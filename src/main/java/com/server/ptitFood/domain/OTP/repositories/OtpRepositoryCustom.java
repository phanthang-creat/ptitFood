package com.server.ptitFood.domain.OTP.repositories;

import com.server.ptitFood.domain.OTP.entities.OTP;

import java.util.List;

public interface OtpRepositoryCustom {
    List<OTP> findByEmailOrUserName(String email, String userName);
}
