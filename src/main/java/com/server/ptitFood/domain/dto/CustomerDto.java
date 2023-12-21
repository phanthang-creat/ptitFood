package com.server.ptitFood.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto implements java.io.Serializable {
  private String username;
    private String password;
    private String name;
    private String phone;
    private String address;
    private String email;
    private String avatar;
    private String role;

    public CustomerDto() {
    }

    public CustomerDto(String _username, String _password, String _name, String _phone, String _address, String _email, String _avatar, String _role) {
        this.username = _username;
        this.password = _password;
        this.name = _name;
        this.phone = _phone;
        this.address = _address;
        this.email = _email;
        this.avatar = _avatar;
        this.role = _role;
    }

    public void setUsername(String _username) {
        this.username = _username;
    }

    public void setPassword(String _password) {
        this.password = _password;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public void setPhone(String _phone) {
        this.phone = _phone;
    }

    public void setAddress(String _address) {
        this.address = _address;
    }

    public void setEmail(String _email) {
        this.email = _email;
    }

    public void setAvatar(String _avatar) {
        this.avatar = _avatar;
    }

    public void setRole(String _role) {
        this.role = _role;
    }
}
