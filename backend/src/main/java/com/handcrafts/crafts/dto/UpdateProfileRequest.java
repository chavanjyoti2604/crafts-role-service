package com.handcrafts.crafts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Schema(description = "Updated full name of the user", example = "Arya Pawar", required = true)
    private String name;

    @Schema(description = "Updated password for the account", example = "NewSecurePass456", required = true)
    private String password;
}
