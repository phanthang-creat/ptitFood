package com.server.ptitFood.domain.dto;

import com.server.ptitFood.common.validations.fullname.ValidVietnameseFullName;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ProfileDto extends InitialDto implements java.io.Serializable {

    @ValidVietnameseFullName(message = "Full name is invalid.")
    private String fullname;

    private String phone;
}
