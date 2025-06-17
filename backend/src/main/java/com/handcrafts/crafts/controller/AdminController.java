package com.handcrafts.crafts.controller;

import com.handcrafts.crafts.dto.LoginRequest;
import com.handcrafts.crafts.dto.RegisterRequest;
import com.handcrafts.crafts.dto.UpdateProfileRequest;
import com.handcrafts.crafts.entity.Admin;
import com.handcrafts.crafts.entity.Seller;
import com.handcrafts.crafts.enums.UserRole;
import com.handcrafts.crafts.security.JwtService;
import com.handcrafts.crafts.service.AdminService;
import com.handcrafts.crafts.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin Controller", description = "Endpoints for Admin registration, login, profile, and seller management")
@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Register a new admin")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(request.getPassword());
        return ResponseEntity.ok(adminService.registerAdmin(admin));
    }

    @PostMapping("/login")
    @Operation(summary = "Login as admin")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        Admin admin = adminService.getAdminByEmail(request.getEmail());
        if (admin != null && passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            return ResponseEntity.ok(jwtService.generateToken(admin.getEmail(), UserRole.ADMIN.toString()));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @GetMapping("/profile")
    @Operation(summary = "Get admin profile")
    public ResponseEntity<Admin> getProfile(HttpServletRequest request) {
        String email = jwtService.extractEmailFromRequest(request);
        return ResponseEntity.ok(adminService.getAdminByEmail(email));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update admin profile")
    public ResponseEntity<String> updateProfile(HttpServletRequest request, @RequestBody UpdateProfileRequest updateRequest) {
        String email = jwtService.extractEmailFromRequest(request);
        Admin updatedAdmin = new Admin();
        updatedAdmin.setName(updateRequest.getName());
        updatedAdmin.setPassword(updateRequest.getPassword());
        adminService.updateProfile(email, updatedAdmin);
        return ResponseEntity.ok("Profile updated");
    }

    @GetMapping("/pending-sellers")
    @Operation(summary = "Get list of pending sellers")
    public ResponseEntity<List<Seller>> getPendingSellers() {
        return ResponseEntity.ok(sellerService.getPendingSellers());
    }

    @GetMapping("/approved-sellers")
    @Operation(summary = "Get list of approved sellers")
    public ResponseEntity<List<Seller>> getApprovedSellers() {
        return ResponseEntity.ok(sellerService.getApprovedSellers());
    }

    @PutMapping("/approve/{id}")
    @Operation(summary = "Approve a seller by ID")
    public ResponseEntity<String> approveSeller(
            @Parameter(description = "ID of the seller to approve") @PathVariable int id) {
        if (sellerService.approveSeller(id)) {
            return ResponseEntity.ok("Seller approved");
        }
        return ResponseEntity.status(404).body("Seller not found or already approved");
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout admin by invalidating JWT token")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = jwtService.extractToken(request);
        jwtService.invalidateToken(token);
        return ResponseEntity.ok("Logged out");
    }
}
