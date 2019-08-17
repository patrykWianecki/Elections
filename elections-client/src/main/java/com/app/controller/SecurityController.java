package com.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.model.dto.security.UserDto;
import com.app.service.SecurityService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("error", "");
        model.addAttribute("user", UserDto.builder().build());
        return "security/loginPage";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute UserDto user) {
        securityService.login(user);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout() {
        securityService.logout();
        return "redirect:/";
    }

    @GetMapping("/login/errorPage")
    public String loginError(Model model) {
        model.addAttribute("error", "WRONG LOGIN OR PASSWORD");
        return "security/loginPage";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "security/accessDenied";
    }
}
