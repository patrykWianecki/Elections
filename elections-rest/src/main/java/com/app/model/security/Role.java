package com.app.model.security;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String fullName;

    Role(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
