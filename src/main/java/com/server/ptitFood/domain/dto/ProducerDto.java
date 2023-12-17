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

    private String keywork;

    private Integer status;

    private Integer trash;

    private Admin createdBy;

    private Admin updatedBy;

    private Date created;

    private Date updated;

    public ProducerDto() {
    }

    public ProducerDto(String _name, String _code, String _keywork, Integer _status, Integer _trash) {
        super();
        this.name = _name;
        this.code = _code;
        this.keywork = _keywork;
        this.status = _status;
        this.trash = _trash;
    }

    public ProducerDto(Integer _id, String _name, String _code, String _keywork, Integer _status, Integer _trash) {
        super();
        this.id = _id;
        this.name = _name;
        this.code = _code;
        this.keywork = _keywork;
        this.status = _status;
        this.trash = _trash;
    }


}
