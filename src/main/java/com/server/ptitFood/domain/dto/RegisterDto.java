package com.server.ptitFood.domain.dto;

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

    private String password;

    private String confirmPassword;

    @Email(message = "Email is invalid.")
    @NotNull(message = "Email can not empty.")
    private String email;

    @ValidVietnameseFullName(message = "Full name is invalid.")
    private String fullName;

    public RegisterDto() {
    }

    public RegisterDto(String _username, String _email, String _fullName) {
        this.username = _username;
        this.email = _email;
        this.fullName = _fullName;
    }
}
