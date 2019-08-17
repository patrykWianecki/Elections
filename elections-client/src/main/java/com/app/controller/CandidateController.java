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

import com.app.model.Gender;
import com.app.model.dto.CandidateDto;
import com.app.service.CandidateService;
import com.app.service.ConstituencyService;
import com.app.validators.CandidateValidator;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/candidates")
public class CandidateController {

    private final CandidateService candidateService;
    private final ConstituencyService constituencyService;
    private final CandidateValidator validator;

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(validator);
    }

    @GetMapping("/add")
    public String addCandidate(Model model) {
        model.addAttribute("candidate", new CandidateDto());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("constituencies", constituencyService.getAllConstituencies());
        model.addAttribute("errors", new HashMap<>());
        return "candidates/add";
    }

    @PostMapping("/add")
    public String addCandidatePost(@ModelAttribute @Valid CandidateDto candidateDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            createModelAttributes(candidateDto, bindingResult, model);
            return "candidates/add";
        }
        candidateService.addCandidate(candidateDto);
        return "redirect:/candidates";
    }

    @GetMapping("/edit/{id}")
    public String editCandidate(@PathVariable Long id, Model model) {
        model.addAttribute("candidate", candidateService.getOneCandidate(id));
        model.addAttribute("genders", Gender.values());
        model.addAttribute("constituencies", constituencyService.getAllConstituencies());
        model.addAttribute("errors", new HashMap<>());
        return "candidates/edit";
    }

    @PostMapping("/edit")
    public String editCandidatePost(@ModelAttribute @Valid CandidateDto candidateDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            createModelAttributes(candidateDto, bindingResult, model);
            return "candidates/edit";
        }
        candidateService.updateCandidate(candidateDto);
        return "redirect:/candidates";
    }

    @PostMapping("/delete")
    public String deleteCandidate(@RequestParam Long id) {
        candidateService.deleteCandidate(id);
        return "redirect:/candidates";
    }

    @GetMapping("/{id}")
    public String showOneCandidate(@PathVariable Long id, Model model) {
        model.addAttribute("candidate", candidateService.getOneCandidate(id));
        return "candidates/one";
    }

    @GetMapping
    public String showAllCandidates(Model model) {
        model.addAttribute("candidates", candidateService.getAllCandidates());
        return "candidates/all";
    }

    private void createModelAttributes(CandidateDto candidateDto, BindingResult bindingResult, Model model) {
        model.addAttribute("candidate", candidateDto);
        model.addAttribute("genders", Gender.values());
        model.addAttribute("constituencies", constituencyService.getAllConstituencies());
        model.addAttribute("errors", bindingResult.getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getCode,
                (v1, v2) -> String.join(", ", v1, v2)))
        );
    }
}
