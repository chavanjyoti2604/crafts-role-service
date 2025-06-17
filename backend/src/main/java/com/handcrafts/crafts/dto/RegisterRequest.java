package com.handcrafts.crafts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterRequest {

    @Schema(description = "Full name of the user", example = "Arya Pawar", required = true)
    private String name;

    @Schema(description = "Email address for registration", example = "arya@example.com", required = true)
    private String email;

    @Schema(description = "Password for the account", example = "MySecurePass123", required = true)
    private String password;
}
