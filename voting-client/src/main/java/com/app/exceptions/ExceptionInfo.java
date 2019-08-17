package com.app.exceptions;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
class ExceptionInfo {

    private String description;
    private LocalDateTime dateTime;

    ExceptionInfo(String description) {
        this.description = description;
        this.dateTime = LocalDateTime.now();
    }
}
