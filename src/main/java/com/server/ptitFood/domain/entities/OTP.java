package com.server.ptitFood.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "db_otp")
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Getter
    @Column(nullable = false, name = "username", columnDefinition = "VARCHAR(255) NOT NULL")
    private String userName;

    @Column(nullable = false, name = "email", columnDefinition = "VARCHAR(255) NOT NULL")
    private String email;

    @Column(nullable = false, name = "otp", length = 6, columnDefinition = "VARCHAR(6)")
    private String otp;

    @Column(nullable = false, name = "status", columnDefinition = "INT(1) DEFAULT 0")
    private Boolean status;

    @Column(nullable = false, name = "created", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date created;

}
