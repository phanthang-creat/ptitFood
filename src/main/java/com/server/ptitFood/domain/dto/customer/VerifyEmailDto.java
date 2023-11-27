package com.server.ptitFood.domain.dto.customer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.springframework.format.annotation.NumberFormat;

@Getter

public class VerifyEmailDto {
    @NotEmpty(message = "Email can not empty.")
    private String email;

    @NotEmpty(message = "OTP can not empty.")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private String otp;

}
