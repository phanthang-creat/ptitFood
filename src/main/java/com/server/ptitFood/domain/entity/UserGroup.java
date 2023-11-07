package com.server.ptitFood.domain.entity;

import com.server.ptitFood.domain.User.models.entity.User;
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

    @Column(nullable = false, name = "name", length = 255)
    private String name;

    @Column(nullable = false, name = "created", columnDefinition = "DATETIME")
    private Date created;

    @Column(nullable = false, name = "created_by", columnDefinition = "INT(11)")
    private Integer createdBy;

    @Column(nullable = false, name = "modified", columnDefinition = "DATETIME")
    private Date modified;

    @Column(nullable = false, name = "modified_by", columnDefinition = "INT(11)")
    private Integer modifiedBy;

    @Column(nullable = false, name = "trash", columnDefinition = "TINYINT(1)")
    private Boolean trash;

    @Column(nullable = false, name = "access", columnDefinition = "TINYINT(1)")
    private Boolean access;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT(1)")
    private Boolean status;

    @OneToMany(mappedBy = "userGroup")
    private List<User> users;
}
