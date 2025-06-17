package com.handcrafts.crafts.entity;

import com.handcrafts.crafts.enums.SellerStatus;
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
@Table(name = "sellers")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique ID of the seller", example = "1")
    private Integer id;

    @Schema(description = "Full name of the seller", example = "Ravi Kumar")
    private String name;

    @Column(unique = true)
    @Schema(description = "Unique email address of the seller", example = "seller@example.com")
    private String email;

    @Schema(description = "Password of the seller", example = "hashedPassword123")
    private String password;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Role of the seller", example = "SELLER", defaultValue = "SELLER")
    private UserRole role = UserRole.SELLER;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Approval status of the seller", example = "PENDING", defaultValue = "PENDING")
    private SellerStatus status = SellerStatus.PENDING;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
