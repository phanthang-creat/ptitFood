package com.server.ptitFood.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto extends InitialDto implements java.io.Serializable {
    private Integer id;

    private String name;

    private String link;

    private Integer parentId;

    private Integer sortDesc;

    private Integer status;

    public CategoryDto() {
    }

    public CategoryDto(String _name, String _link, Integer _parentId, Integer _sortDesc, Integer _status) {
        this.name = _name;
        this.link = _link;
        this.parentId = _parentId;
        this.sortDesc = _sortDesc;
        this.status = _status;
    }

}
