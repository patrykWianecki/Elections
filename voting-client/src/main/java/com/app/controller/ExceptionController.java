package com.app.controller;

import java.time.LocalDateTime;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.app.exceptions.MyException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MyException.class)
    public String myExceptionHandler(MyException e, Model model) {
        model.addAttribute("description", e.getExceptionInfo());
        model.addAttribute("dateTime", LocalDateTime.now());
        return "errorPage";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String notFoundException(MyException e, Model model) {
        model.addAttribute("description", e.getExceptionInfo());
        model.addAttribute("dateTime", LocalDateTime.now());
        return "errorPage";
    }
}
