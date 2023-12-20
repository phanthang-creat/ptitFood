package com.server.ptitFood.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@Data
public class VerifyEmailDto {
    @NotEmpty(message = "Email can not empty.")
    private String email;

    @NotEmpty(message = "OTP can not empty.")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private String otp;

    public VerifyEmailDto() {
    }

    public VerifyEmailDto(String _email) {
        this.email = _email;
    }

}
