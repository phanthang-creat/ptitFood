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
@Table(name = "db_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "name", columnDefinition = "VARBINARY(600) NOT NULL")
    private String name;

    @Column(nullable = false, name = "link", columnDefinition = "VARBINARY(600) NOT NULL")
    private String link;

    @Column(nullable = false, name = "level", columnDefinition = "INT NOT NULL DEFAULT 0")
    private Integer level;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Category parentId;

    @Column(nullable = false, name = "orders", columnDefinition = "VARCHAR(5)")
    private String orders;

    @Column(nullable = false, name = "created_at", columnDefinition = "DATETIME")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private Admin createdBy;

    @Column(nullable = false, name = "updated_at", columnDefinition = "DATETIME")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private Admin updatedBy;

    @Column(nullable = false, name = "status", columnDefinition = "INT(1)")
    private Boolean status;
}
