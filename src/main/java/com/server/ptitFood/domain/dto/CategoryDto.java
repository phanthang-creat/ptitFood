package com.server.ptitFood.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class CategoryDto extends InitialDto implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String link;

    private Integer parentId;

    private Integer order;

    private Integer status;

    public CategoryDto() {
    }

    public CategoryDto(String _name, String _link, Integer _parentId, Integer _order, Integer _status) {
        this.name = _name;
        this.link = _link;
        this.parentId = _parentId;
        this.order = _order;
        this.status = _status;
    }

}
