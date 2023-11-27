package com.server.ptitFood.domain.dto.customer;

import com.server.ptitFood.common.validations.password.ValidPassword;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDto {
    @NotEmpty(message = "Username can not empty.")
    private String username;

    @ValidPassword(message = "Password is invalid.")
    private String password;
}
