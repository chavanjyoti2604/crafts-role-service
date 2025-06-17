package com.handcrafts.crafts.controller;

import com.handcrafts.crafts.dto.LoginRequest;
import com.handcrafts.crafts.dto.RegisterRequest;
import com.handcrafts.crafts.dto.UpdateProfileRequest;
import com.handcrafts.crafts.entity.Seller;
import com.handcrafts.crafts.enums.UserRole;
import com.handcrafts.crafts.security.JwtService;
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

import java.util.Map;

@Tag(name = "Seller Controller", description = "Endpoints for Seller registration, login, profile and status")
@RestController
@RequestMapping("/seller")
@CrossOrigin(origins = "http://localhost:3000")
@SecurityRequirement(name = "bearerAuth")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Register a new seller")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        Seller seller = new Seller();
        seller.setName(request.getName());
        seller.setEmail(request.getEmail());
        seller.setPassword(request.getPassword());
        return ResponseEntity.ok(sellerService.registerSeller(seller));
    }

    @PostMapping("/login")
    @Operation(summary = "Login as a seller")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Seller seller = sellerService.getSellerByEmail(request.getEmail());
        boolean match = seller != null && passwordEncoder.matches(request.getPassword(), seller.getPassword());
        if (match) {
            String token = jwtService.generateToken(seller.getEmail(), UserRole.SELLER.toString());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
    }

    @GetMapping("/profile")
    @Operation(summary = "Get current seller profile")
    public ResponseEntity<Seller> getProfile(@Parameter(hidden = true) HttpServletRequest request) {
        String email = jwtService.extractEmailFromRequest(request);
        return ResponseEntity.ok(sellerService.getSellerByEmail(email));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update seller profile")
    public ResponseEntity<String> updateProfile(
            @Parameter(hidden = true) HttpServletRequest request,
            @RequestBody UpdateProfileRequest updateRequest) {
        String email = jwtService.extractEmailFromRequest(request);
        Seller updatedSeller = new Seller();
        updatedSeller.setName(updateRequest.getName());
        updatedSeller.setPassword(updateRequest.getPassword());
        sellerService.updateProfile(email, updatedSeller);
        return ResponseEntity.ok("Profile updated");
    }

    @GetMapping("/status")
    @Operation(summary = "Get seller approval status")
    public ResponseEntity<String> getStatus(@Parameter(hidden = true) HttpServletRequest request) {
        String email = jwtService.extractEmailFromRequest(request);
        return ResponseEntity.ok(sellerService.getStatus(email));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout seller and invalidate JWT")
    public ResponseEntity<String> logout(@Parameter(hidden = true) HttpServletRequest request) {
        String token = jwtService.extractToken(request);
        jwtService.invalidateToken(token);
        return ResponseEntity.ok("Logged out");
    }
}
