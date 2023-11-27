package com.server.ptitFood.domain.entities;

import com.server.ptitFood.domain.entities.Admin;
import jakarta.persistence.*;

import java.sql.Date;

@Table(name = "db_slider")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "name", columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(nullable = false, name = "link", columnDefinition = "VARCHAR(255) NOT NULL")
    private String link;

    @Column(nullable = false, name = "img", columnDefinition = "VARCHAR(255) NOT NULL")
    private String img;

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

    @Column(nullable = false, name = "trash", columnDefinition = "INT(1)")
    private Boolean trash;

    @Column(nullable = false, name = "status", columnDefinition = "INT(1)")
    private Boolean status;

}
