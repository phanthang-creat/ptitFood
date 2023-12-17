package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.OTP;

import java.util.List;

public interface OtpRepositoryCustom {
    List<OTP> findByEmailOrUserName(String email, String userName);
}
