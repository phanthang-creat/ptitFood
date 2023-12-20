package com.server.ptitFood.domain.dto;

import com.server.ptitFood.domain.entities.Admin;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.sql.Date;

@Setter
@Getter
public class ProducerDto extends InitialDto implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String code;

    private String keyword;

    private Integer status;

    private Admin createdBy;

    private Admin updatedBy;

    private Date created;

    private Date updated;

    public ProducerDto() {
    }
}
