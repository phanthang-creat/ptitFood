package com.server.ptitFood.domain.entities;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "db_customer")
public class Customer implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "fullname", columnDefinition = "VARCHAR(255)")
    private String fullName;

    @Column(nullable = false, name = "username", columnDefinition = "VARCHAR(100) UNIQUE NOT NULL")
    private String userName;

    @Column(nullable = false, name = "password", length = 600, columnDefinition = "VARBINARY(600) NOT NULL")
    private String password;

    @Column(nullable = false, name = "email", columnDefinition = "VARBINARY(600) UNIQUE NOT NULL")
    private String email;

    @Column(nullable = false, name = "phone", length = 15, columnDefinition = "VARBINARY(600)")
    private String phone;

    @Column(nullable = false, name = "otp", columnDefinition = "VARCHAR(6)")
    private String otp;

    @Column(nullable = false, name = "created", columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    @Column(nullable = false, name = "trash", columnDefinition = "INT(1) DEFAULT 0")
    private Integer trash;

    @Column(nullable = false, name = "status", columnDefinition = "INT(1) DEFAULT 1")
    private Integer status;

    @Column(nullable = false, name = "updated", columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updated;

    @ManyToOne
    @JoinColumn(name = "role", nullable = false, referencedColumnName = "id")
    private UserGroup userGroup;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isEnabled() {
        return this.status == 1;
    }
}
