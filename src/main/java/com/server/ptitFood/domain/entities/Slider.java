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
public class Slider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "name", columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(nullable = false, name = "code", columnDefinition = "VARCHAR(255) NOT NULL")
    private String link;

    @Column(nullable = false, name = "keyword", columnDefinition = "VARCHAR(255) NOT NULL")
    private Integer level;

    @Column(nullable = false, name = "created_at", columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private Admin createdBy;

    @Column(nullable = false, name = "updated", columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private Admin updatedBy;

    @Column(nullable = false, name = "status", columnDefinition = "INT(1)")
    private Boolean status;

    @Column(nullable = false, name = "trash", columnDefinition = "INT(1) DEFAULT 0")
    private Boolean trash;
}
