package com.server.ptitFood.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

    @Column(nullable = false, name = "name", columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(nullable = false, name = "code", columnDefinition = "VARCHAR(255) NOT NULL")
    private String code;

    @Column(nullable = false, name = "keyword", columnDefinition = "VARCHAR(255) NOT NULL")
    private String keyword;

    @CreatedDate()
    @Column(nullable = false, name = "created", columnDefinition = "DATETIME")
    private Date created;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private Admin createdBy;

    @LastModifiedDate()
    @Column(nullable = false, name = "updated", columnDefinition = "DATETIME")
    private Date updated;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private Admin updatedBy;

    @Column(nullable = false, name = "status", columnDefinition = "INT(1)")
    private Integer status;
}
