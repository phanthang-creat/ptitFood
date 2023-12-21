package com.server.ptitFood.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDto {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;
}
