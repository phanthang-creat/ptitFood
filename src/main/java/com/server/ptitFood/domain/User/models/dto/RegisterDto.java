package com.server.ptitFood.domain.User.models.dto;

import com.server.ptitFood.common.validations.fullname.ValidVietnameseFullName;
import com.server.ptitFood.common.validations.password.ValidPassword;
import com.server.ptitFood.common.validations.username.ValidUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class RegisterDto {

    @ValidUsername(message = "Username is invalid.")
    private String username;

    @ValidPassword(message = "Password is invalid.")
    private String password;

    @ValidPassword(message = "Confirm Password is invalid.")
    private String confirmPassword;

    @Email(message = "Email is invalid.")
    @NotNull(message = "Email can not empty.")
    private String email;

    @ValidVietnameseFullName(message = "Full name is invalid.")
    private String fullName;
}
