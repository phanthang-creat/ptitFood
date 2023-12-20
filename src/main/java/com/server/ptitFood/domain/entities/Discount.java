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
@Table(name = "db_discount")
public class Discount {
        @Id()
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(nullable = false, name = "id", columnDefinition = "INT(11)")
        private Integer id;

        @Column(nullable = false, name = "code", columnDefinition = "VARCHAR(255)")
        private String code;

        @Column(nullable = false, name = "discount", columnDefinition = "INT(11)")
        private Integer discount;

        @Column(nullable = false, name = "limit_number", columnDefinition = "INT(11)")
        private Integer limitNumber;

        @Column(nullable = false, name = "number_used", columnDefinition = "INT(11)")
        private Integer numberUsed;

        @Column(nullable = false, name = "expiration_date", columnDefinition = "DATETIME")
        private Date expirationDate;

        @Column(nullable = false, name = "payment_limit", columnDefinition = "INT(11)")
        private Integer paymentLimit;

        @Column(nullable = false, name = "description", columnDefinition = "VARCHAR(500)")
        private String description;

        @Column(nullable = false, name = "orders", columnDefinition = "INT(11)")
        private Integer orders;

        @Column(nullable = false, name = "created", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
        private Date created;

        @Column(nullable = false, name = "updated", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
        private Date updated;

        @Column(nullable = false, name = "status", columnDefinition = "INT(1)")
        private Integer status;

        @ManyToOne
        @JoinColumn(name = "created_by", referencedColumnName = "id")
        private Admin createdBy;

        @ManyToOne
        @JoinColumn(name = "updated_by", referencedColumnName = "id")
        private Admin updatedBy;
}
