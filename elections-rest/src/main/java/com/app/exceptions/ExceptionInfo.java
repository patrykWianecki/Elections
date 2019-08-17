package com.app.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
class ExceptionInfo {

    private String description;
    private LocalDateTime dateTime;

    ExceptionInfo(String description) {
        this.description = description;
        this.dateTime = LocalDateTime.now();
    }
}
