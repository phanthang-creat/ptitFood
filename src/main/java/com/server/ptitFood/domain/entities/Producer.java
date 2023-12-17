package com.server.ptitFood.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "db_producer")
public class Producer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "name", columnDefinition = "VARBINARY(600) NOT NULL")
    private String name;

    @Column(nullable = false, name = "code", columnDefinition = "VARBINARY(600) NOT NULL")
    private String code;

    @Column(nullable = false, name = "keyword", columnDefinition = "varbinary(600) NOT NULL")
    private String keyword;

    @Column(nullable = false, name = "created", columnDefinition = "DATETIME")
    private Date created;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private Admin createdBy;

    @Column(nullable = false, name = "updated", columnDefinition = "DATETIME")
    private Date updated;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private Admin updatedBy;

    @Column(nullable = false, name = "status", columnDefinition = "INT(1)")
    private Integer status;

    @Column(nullable = false, name = "trash", columnDefinition = "INT(1)")
    private Integer trash;
}
