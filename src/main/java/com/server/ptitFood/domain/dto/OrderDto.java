package com.server.ptitFood.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    private Integer id;
    private String customerId;
    private String fullname;
    private String phone;
    private Integer money;
    private String codeDiscount;
    private String address;
    private Integer status;
}
