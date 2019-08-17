package com.app.controller;

import java.util.HashMap;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.model.dto.security.UserDto;
import com.app.model.security.Role;
import com.app.service.UserService;
import com.app.validators.UserValidator;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserValidator validator;

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(validator);
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        model.addAttribute("user", new UserDto());
        model.addAttribute("role", Role.values());
        model.addAttribute("errors", new HashMap<>());
        return "users/add";
    }

    @PostMapping("/add")
    public String addUserPost(@ModelAttribute @Valid UserDto user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            createModelAttributes(user, bindingResult, model);
            return "users/add";
        }
        userService.addUser(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getOneUser(id));
        model.addAttribute("role", Role.values());
        model.addAttribute("errors", new HashMap<>());
        return "users/edit";
    }

    @PostMapping("/edit")
    public String updateUserPost(@ModelAttribute @Valid UserDto user, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            createModelAttributes(user, bindingResult, model);
            return "users/edit";
        }
        userService.updateUser(user);
        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("/{id}")
    public String showOneUser(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getOneUser(id));
        return "users/one";
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/all";
    }

    private void createModelAttributes(UserDto userDto, BindingResult bindingResult, Model model) {
        model.addAttribute("user", new UserDto());
        model.addAttribute("role", Role.values());
        model.addAttribute("errors", bindingResult.getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getCode,
                (v1, v2) -> String.join(". ", v1, v2)))
        );
    }
}