package com.handcrafts.crafts.enums;

public enum UserRole {
    USER,
    SELLER,
    ADMIN;

    @Override
    public String toString() {
        return name();
    }
}
