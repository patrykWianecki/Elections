package com.app.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.model.dto.TokenFromVoterDto;
import com.app.model.dto.VoterDto;
import com.app.model.dto.VotingResult;
import com.app.service.VotingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voting")
public class VotingController {

    private final VotingService votingService;

    @GetMapping("/confirm")
    public String confirmVoting(Model model) {
        model.addAttribute("error", "");
        model.addAttribute("tokenFromVoter", new TokenFromVoterDto());
        return "voting/confirm";
    }

    @PostMapping("/confirm")
    public String confirmVotingPost(@ModelAttribute TokenFromVoterDto tokenFromVoterDto, Model model, RedirectAttributes attributes) {
        VoterDto voterDto = votingService.findVoterByToken(String.valueOf(tokenFromVoterDto.getToken()));
        model.addAttribute("voterDto", voterDto);
        attributes.addFlashAttribute(tokenFromVoterDto.getToken());
        return "redirect:/voting/vote";
    }

    @GetMapping("/confirm/errorPage")
    public String incorrectToken(Model model) {
        model.addAttribute("error", "INCORRECT TOKEN");
        return "voting/confirm";
    }

    @GetMapping("/vote")
    public String vote(@ModelAttribute String token, Model model) {
        VotingResult votingResult = VotingResult.builder().token(Integer.valueOf(token)).build();
        model.addAttribute("votingResult", votingResult);
        model.addAttribute("candidates", votingService.findAllCandidatesForVoterWithGivenToken(token));
        return "voting/vote";
    }

    @PostMapping("/vote")
    public String votePost(@ModelAttribute VotingResult votingResult) {
        votingResult
            .getCandidateId()
            .forEach(result -> votingService.updateCandidate(votingService.findCandidateById(result)));
        votingService.removeUsedToken(votingResult.getToken());
        return "redirect:/voting/finish";
    }

    @GetMapping("/finish")
    public String finishVoting() {
        return "voting/finish";
    }

    @GetMapping("/secondBallot")
    public String secondBallot() {
        votingService.secondBallot();
        return "voting/secondBallot";
    }

    @GetMapping("/result")
    public String showWinnersFromEachConstituency(Model model) {
        model.addAttribute("winners", votingService.findWinnersInEachConstituency());
        model.addAttribute("avgVotersAge", votingService.countAverageVotersAge());
        model.addAttribute("votersGenderPercentage", votingService.countGenderPercentage());
        model.addAttribute("candidatesPercentageResult", votingService.groupCandidatesWithPercentageResultsInConstituencies());
        return "voting/result";
    }
}
