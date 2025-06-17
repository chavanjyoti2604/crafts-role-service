package com.handcrafts.crafts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SellerStatusResponse {

    @Schema(description = "Full name of the seller", example = "John Doe")
    private String name;

    @Schema(description = "Email address of the seller", example = "seller@example.com")
    private String email;

    @Schema(description = "Current approval status of the seller", example = "PENDING")
    private String status;
}
