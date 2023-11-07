package com.server.ptitFood.domain.User.models.entity;
import com.server.ptitFood.domain.entity.UserGroup;
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
@Table(name = "db_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "fullname", columnDefinition = "VARCHAR(255)")
    private String fullName;

    @Getter
    @Column(nullable = false, name = "username", columnDefinition = "VARCHAR(255)")
    private String userName;

    @Column(nullable = false, name = "password", length = 64, columnDefinition = "VARCHAR(64)")
    private String password;

    @Column(nullable = false, name = "email", columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(nullable = false, name = "gender", columnDefinition = "INT(1)")
    private Integer gender;

    @Column(nullable = false, name = "phone", length = 15, columnDefinition = "VARCHAR(15)")
    private String phone;

    @Column(nullable = false, name = "address", columnDefinition = "VARCHAR(255)")
    private String address;

    @Column(nullable = false, name = "img", columnDefinition = "VARCHAR(255)")
    private String img;

    @Column(nullable = false, name = "created", columnDefinition = "DATETIME")
    private Date created;

    @Column(nullable = false, name = "trash", columnDefinition = "INT(1)")
    private Boolean trash;

    @Column(nullable = false, name = "status", columnDefinition = "INT(1)")
    private Boolean status;

    @Column(nullable = false, name = "updated", columnDefinition = "DATETIME")
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
        return this.status;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.status;
    }

    @Override
    public boolean isEnabled() {
        return this.status;
    }
}
