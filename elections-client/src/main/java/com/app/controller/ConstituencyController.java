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

import com.app.model.dto.ConstituencyDto;
import com.app.service.ConstituencyService;
import com.app.validators.ConstituencyValidator;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/constituencies")
public class ConstituencyController {

    private final ConstituencyService constituencyService;
    private final ConstituencyValidator validator;

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(validator);
    }

    @GetMapping("/add")
    public String addConstituency(Model model) {
        model.addAttribute("constituency", new ConstituencyDto());
        model.addAttribute("errors", new HashMap<>());
        return "constituencies/add";
    }

    @PostMapping("/add")
    public String addConstituencyPost(@ModelAttribute @Valid ConstituencyDto constituencyDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            createModelAttributes(constituencyDto, bindingResult, model);
            return "constituencies/add";
        }
        constituencyService.addConstituency(constituencyDto);
        return "redirect:/constituencies";
    }

    @GetMapping("/edit/{id}")
    public String editConstituency(@PathVariable Long id, Model model) {
        model.addAttribute("constituency", constituencyService.getOneConstituency(id));
        model.addAttribute("errors", new HashMap<>());
        return "constituencies/edit";
    }

    @PostMapping("/edit")
    public String editConstituenciesPost(@ModelAttribute @Valid ConstituencyDto constituencyDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            createModelAttributes(constituencyDto, bindingResult, model);
            return "constituencies/edit";
        }
        constituencyService.updateConstituency(constituencyDto);
        return "redirect:/constituencies";
    }

    @PostMapping("/delete")
    public String deleteConstituency(@RequestParam Long id) {
        constituencyService.deleteConstituency(id);
        return "redirect:/constituencies";
    }

    @GetMapping("/{id}")
    public String showOneConstituency(@PathVariable Long id, Model model) {
        model.addAttribute("constituency", constituencyService.getOneConstituency(id));
        return "constituencies/one";
    }

    @GetMapping
    public String showAllConstituencies(Model model) {
        model.addAttribute("constituencies", constituencyService.getAllConstituencies());
        return "constituencies/all";
    }

    private void createModelAttributes(ConstituencyDto constituencyDto, BindingResult bindingResult, Model model) {
        model.addAttribute("constituency", constituencyDto);
        model.addAttribute("errors", bindingResult.getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getCode,
                (v1, v2) -> String.join(", ", v1, v2)))
        );
    }
}
