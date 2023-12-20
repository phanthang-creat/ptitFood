package com.server.ptitFood.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class ProductDto extends InitialDto implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String alias;

    private String detail;

    private String avatar;

    private String img;

    private Integer sortDesc;

    private Integer price = 0;

    private Integer priceSale = 0;

    private Integer number = 0;

    private Integer numberBuy = 0;

    private Integer catId;

    private Integer producerId;

    private Integer status;

    public ProductDto() {
    }
}
