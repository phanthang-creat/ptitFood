package com.server.ptitFood.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "db_usergroup")
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "role_name", length = 255)
    private String name;

    @Column(nullable = false, name = "created", columnDefinition = "DATETIME")
    private Date created;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private UserGroup createdBy;

    @Column(nullable = false, name = "updated", columnDefinition = "DATETIME")
    private Date modified;

    @Column(nullable = false, name = "updated_by", columnDefinition = "INT(11)")
    private Integer modifiedBy;

    @Column(nullable = false, name = "trash", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Integer trash;

    @Column(nullable = false, name = "access", columnDefinition = "INT(1) DEFAULT 1")
    private Integer access;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Integer status;

    @OneToMany(mappedBy = "userGroup")
    private List<Customer> users;
}
