package com.server.ptitFood.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.sql.Date;

@Table(name = "db_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cat_id", referencedColumnName = "id")
    private Category category;

    @Column(nullable = false, name = "name", columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(nullable = false, name = "alias", columnDefinition = "VARCHAR(255) NOT NULL")
    private String alias;

    @Column(nullable = false, name = "img", columnDefinition = "VARCHAR(255) NOT NULL")
    private String img;

    @Column(nullable = false, name = "avatar", columnDefinition = "VARCHAR(255) NOT NULL")
    private String avatar;

    @Column(nullable = false, name = "sort_desc", columnDefinition = "INT")
    private Integer sortDesc;

    @Column(nullable = false, name = "detail", columnDefinition = "TEXT")
    private String detail;

    @ManyToOne
    @JoinColumn(name = "producer" , referencedColumnName = "id")
    private Producer producer;

    @Column(nullable = false, name = "price", columnDefinition = "INT(11) NOT NULL")
    private Integer price;

    @Column(nullable = false, name = "price_sale", columnDefinition = "INT(11)")
    private Integer priceSale;

    @Column(nullable = false, name = "number", columnDefinition = "INT(11) NOT NULL")
    private Integer number;

    @Column(nullable = false, name = "number_buy", columnDefinition = "INT(11) NOT NULL")
    private Integer numberBuy;

    @Column(nullable = false, name = "created", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private Admin createdBy;

    @Column(nullable = false, name = "updated", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updated;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private Admin updatedBy;

    @Column(nullable = false, name = "status", columnDefinition = "INT(1)")
    private Integer status;
}
