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

        @Column(nullable = false, name = "phone", columnDefinition = "VARCHAR(255)")
        private String phone;

        @Column(nullable = false, name = "address", columnDefinition = "VARCHAR(255)")
        private String address;

        @Column(nullable = false, name = "money", columnDefinition = "INT")
        private Integer money;

        @ManyToOne
        @JoinColumn(name = "code_discount", referencedColumnName = "id")
        private Discount discount;

        @ManyToOne()
        @JoinColumn(name = "status", referencedColumnName = "id")
        private OrderStatus orderStatus;
}
