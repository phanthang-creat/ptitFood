package com.server.ptitFood.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "db_order")
public class Order {

        @Id()
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne
        @JoinColumn(name = "customer_id", referencedColumnName = "id")
        private Customer user;

        @Column(nullable = false, name = "order_date", columnDefinition = "DATETIME")
        private Date orderDate;

        @Column(nullable = false, name = "fullname", columnDefinition = "VARCHAR(100)")
        private String fullname;

        @Column(nullable = false, name = "phone", columnDefinition = "varbinary(600)")
        private String phone;

        @Column(nullable = false, name = "address", columnDefinition = "varbinary(600)")
        private String address;

        @Column(nullable = false, name = "money", columnDefinition = "INT")
        private Integer money;

        @ManyToOne
        @JoinColumn(name = "code_discount", referencedColumnName = "id")
        private Discount discount;

        @Column(nullable = false, name = "status", columnDefinition = "INT")
        private Integer status;
}
