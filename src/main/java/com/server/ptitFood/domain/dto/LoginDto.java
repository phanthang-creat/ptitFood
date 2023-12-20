package com.server.ptitFood.domain.dto;

import com.server.ptitFood.common.validations.password.ValidPassword;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class LoginDto {
    @NotEmpty(message = "Username can not empty.")
    private String username;

//    @ValidPassword(message = "Password is invalid.")
    private String password;

    public LoginDto() {
    }
    public LoginDto(String _username) {
        this.username = _username;
    }
}
