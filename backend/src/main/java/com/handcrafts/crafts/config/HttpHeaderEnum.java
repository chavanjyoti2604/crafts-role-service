package com.handcrafts.crafts.config;

public enum HttpHeaderEnum {
    AUTHORIZATION("Authorization"),
    CONTENT_TYPE("Content-Type"),
    CACHE_CONTROL("Cache-Control");  // ✅ Added this

    private final String value;

    HttpHeaderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
