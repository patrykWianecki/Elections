package com.app.exceptions;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
class ExceptionInfo {

    private final String description;
    private final LocalDateTime dateTime;

    ExceptionInfo(String description) {
        this.description = description;
        this.dateTime = LocalDateTime.now();
    }
}
