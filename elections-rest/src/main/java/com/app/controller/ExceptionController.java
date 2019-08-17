package com.app.controller;

import java.time.LocalDateTime;

import com.app.exceptions.MyException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ExceptionController {

    // TODO formater dodać
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
