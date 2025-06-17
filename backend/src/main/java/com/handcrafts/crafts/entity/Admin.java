package com.handcrafts.crafts.entity;

import com.handcrafts.crafts.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Admin ID", example = "1")
    private Integer id;

    @Schema(description = "Full name of the admin", example = "Arya Pawar")
    private String name;

    @Column(unique = true)
    @Schema(description = "Unique email address of the admin", example = "admin@example.com")
    private String email;

    @Schema(description = "Password for admin login", example = "encryptedPassword123")
    private String password;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Role assigned to admin", example = "ADMIN", defaultValue = "ADMIN")
    private UserRole role = UserRole.ADMIN;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
