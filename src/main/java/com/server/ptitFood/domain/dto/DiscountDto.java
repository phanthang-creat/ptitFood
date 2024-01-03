package com.server.ptitFood.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class DiscountDto extends InitialDto {
    private String code;

    private Integer discount;

    private Integer limitNumber;

    private Date expirationDate;

    private Integer paymentLimit;

    private String description;

    private Integer status;
}
