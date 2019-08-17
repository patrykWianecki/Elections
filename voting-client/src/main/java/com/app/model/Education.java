package com.app.model;

public enum Education {
    PRIMARY_EDUCATION("PRIMARY EDUCATION"),
    SECONDARY_EDUCATION("SECONDARY EDUCATION"),
    HIGHER_EDUCATION("HIGHER EDUCATION");

    private String value;

    Education(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
