package com.server.ptitFood.domain.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Getter
@Setter
public class InitialDto implements java.io.Serializable {
    private Boolean isEdit = false;

    public InitialDto() {
    }
}
