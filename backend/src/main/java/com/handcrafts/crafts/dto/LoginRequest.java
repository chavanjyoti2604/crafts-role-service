package com.handcrafts.crafts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {

    @Schema(description = "Email address of the user", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "Password of the user", example = "securePassword123", required = true)
    private String password;
}
