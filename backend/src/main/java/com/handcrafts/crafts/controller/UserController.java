package com.handcrafts.crafts.controller;

import com.handcrafts.crafts.dto.LoginRequest;
import com.handcrafts.crafts.dto.RegisterRequest;
import com.handcrafts.crafts.dto.UpdateProfileRequest;
import com.handcrafts.crafts.entity.User;
import com.handcrafts.crafts.enums.UserRole;
import com.handcrafts.crafts.security.JwtService;
import com.handcrafts.crafts.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "User Controller", description = "Handles user registration, login, profile and logout")
@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Registration failed")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    @Operation(summary = "User login")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userService.getUserByEmail(request.getEmail());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }
        String token = jwtService.generateToken(user.getEmail(), UserRole.USER.toString());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/profile")
    @Operation(summary = "Get user profile")
    public ResponseEntity<User> getProfile(@Parameter(hidden = true) HttpServletRequest request) {
        String email = jwtService.extractEmailFromRequest(request);
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update user profile")
    public ResponseEntity<String> updateProfile(
            @Parameter(hidden = true) HttpServletRequest request,
            @RequestBody UpdateProfileRequest updateRequest) {
        String email = jwtService.extractEmailFromRequest(request);
        User updatedUser = new User();
        updatedUser.setName(updateRequest.getName());
        updatedUser.setPassword(updateRequest.getPassword());
        userService.updateProfile(email, updatedUser);
        return ResponseEntity.ok("Profile updated");
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user and invalidate JWT")
    public ResponseEntity<String> logout(@Parameter(hidden = true) HttpServletRequest request) {
        String token = jwtService.extractToken(request);
        jwtService.invalidateToken(token);
        return ResponseEntity.ok("Logged out");
    }
}
