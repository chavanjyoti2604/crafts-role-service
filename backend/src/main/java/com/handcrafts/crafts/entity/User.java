package com.handcrafts.crafts.entity;

import com.handcrafts.crafts.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "User ID", example = "1")
    private Integer id;

    @Schema(description = "Full name of the user", example = "Arya Pawar")
    private String name;

    @Column(unique = true, nullable = false)
    @Schema(description = "Unique email address of the user", example = "user@example.com", required = true)
    private String email;

    @Column(nullable = false)
    @Schema(description = "Password of the user", example = "hashedPassword", required = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Schema(description = "User role", example = "USER", defaultValue = "USER")
    private UserRole role = UserRole.USER; // default role

    // --- UserDetails Implementation ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
