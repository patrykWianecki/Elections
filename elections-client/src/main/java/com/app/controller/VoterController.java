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

import com.app.model.Education;
import com.app.model.Gender;
import com.app.model.dto.VoterDto;
import com.app.service.ConstituencyService;
import com.app.service.VoterService;
import com.app.validators.VoterValidator;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/voters")
public class VoterController {

    private final VoterService voterService;
    private final ConstituencyService constituencyService;
    private final VoterValidator validator;

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(validator);
    }

    @GetMapping("/add")
    public String addVoter(Model model) {
        model.addAttribute("voters", new VoterDto());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("educations", Education.values());
        model.addAttribute("constituencies", constituencyService.getAllConstituencies());
        model.addAttribute("errors", new HashMap<>());
        return "voters/add";
    }

    @PostMapping("/add")
    public String addVoterPost(@ModelAttribute @Valid VoterDto voterDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            createModelAttributes(voterDto, bindingResult, model);
            return "voters/add";
        }
        voterService.addVoter(voterDto);
        return "redirect:/voters";
    }

    @GetMapping("/edit/{id}")
    public String editVoter(@PathVariable Long id, Model model) {
        model.addAttribute("voter", voterService.getOneVoter(id));
        model.addAttribute("genders", Gender.values());
        model.addAttribute("educations", Education.values());
        model.addAttribute("constituencies", constituencyService.getAllConstituencies());
        model.addAttribute("errors", new HashMap<>());
        return "voters/edit";
    }

    @PostMapping("/edit")
    public String editVoterPost(@ModelAttribute @Valid VoterDto voterDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            createModelAttributes(voterDto, bindingResult, model);
            return "voters/edit";
        }
        voterService.updateVoter(voterDto);
        return "redirect:/voters";
    }

    @PostMapping("/delete")
    public String deleteVoter(@RequestParam Long id) {
        voterService.deleteVoter(id);
        return "redirect:/voters";
    }

    @GetMapping("/{id}")
    public String showOneVoter(@PathVariable Long id, Model model) {
        model.addAttribute("voter", voterService.getOneVoter(id));
        return "voters/one";
    }

    @GetMapping
    public String showAllVoters(Model model) {
        model.addAttribute("voters", voterService.getAllVoters());
        return "voters/all";
    }

    private void createModelAttributes(VoterDto voterDto, BindingResult bindingResult, Model model) {
        model.addAttribute("voters", new VoterDto());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("educations", Education.values());
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
