package com.handcrafts.crafts.config;

public enum RoleEnum {
    USER, SELLER, ADMIN;

    public String getAuthority() {
        return name(); // e.g., "USER"
    }
}
