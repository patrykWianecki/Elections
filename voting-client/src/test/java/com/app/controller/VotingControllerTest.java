package com.app.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.model.dto.CandidateDto;
import com.app.model.dto.ConstituencyDto;
import com.app.model.dto.TokenFromVoterDto;
import com.app.service.VotingService;

import static com.app.builders.MockDataForTests.*;
import static com.app.model.Gender.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class VotingControllerTest {

    @MockBean
    private VotingService votingService;
    @MockBean
    private RedirectAttributes redirectAttributes;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_successfully_confirm_voting() throws Exception {
        // given

        // when + then
        mockMvc.perform(get("/voting/confirm"))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("confirmVoting"))
            .andExpect(content().contentType("text/plain;charset=UTF-8"));
        //            .andExpect(view().name("voting/confirm"));
    }

//    @Test
//    void should_successfully_confirm_voting_post() throws Exception {
//        // given
//        when(redirectAttributes.addFlashAttribute(eq(TokenFromVoterDto.class))).thenReturn(null);
//        when(votingService.findVoterByToken(anyString())).thenReturn(createVoterDto());
//
//        // when + then
//        mockMvc.perform(post("/voting/confirm"))
//            .andExpect(status().isOk())
//            .andExpect(handler().methodName("confirmVotingPost"))
//            .andExpect(content().contentType("text/plain;charset=UTF-8"))
//            .andExpect(view().name("redirect:/voting/vote"));
//    }

    @Test
    void should_successfully_handle_incorrect_token() throws Exception {
        // given

        // when + then
        mockMvc.perform(get("/voting/confirm/errorPage"))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("incorrectToken"))
            .andExpect(content().contentType("text/plain;charset=UTF-8"));
        //            .andExpect(view().name("voting/confirm"));
    }

//    @Test
//    void should_successfully_vote() throws Exception {
//        // given
//        when(votingService.findAllCandidatesForVoterWithGivenToken(anyString())).thenReturn(createCandidates());
//
//        // when + then
//        mockMvc.perform(get("/voting/vote"))
//            .andExpect(status().isOk())
//            .andExpect(handler().methodName("vote"))
//            .andExpect(content().contentType("text/plain;charset=UTF-8"))
//            .andExpect(view().name("voting/vote"));
//    }
//
//    @Test
//    void should_successfully_vote_post() throws Exception {
//        // given
//        when(votingService.findCandidateById(anyLong())).thenReturn(createCandidateDto());
//        when(votingService.updateCandidate(any())).thenReturn(createCandidateDto());
//        when(votingService.removeUsedToken(anyInt())).thenReturn(createTokenDto());
//
//        // when + then
//        mockMvc.perform(post("/voting/vote"))
//            .andExpect(status().isOk())
//            .andExpect(handler().methodName("votePost"))
//            .andExpect(content().contentType("text/plain;charset=UTF-8"))
//            .andExpect(view().name("redirect:/voting/finish"));
//    }
//
//    @Test
//    void should_successfully_finish_voting() throws Exception {
//        // given
//
//        // when + then
//        mockMvc.perform(post("/voting/finish"))
//            .andExpect(status().isOk())
//            .andExpect(handler().methodName("finishVoting"))
//            .andExpect(content().contentType("text/plain;charset=UTF-8"))
//            .andExpect(view().name("voting/finish"));
//    }
//
//    @Test
//    void should_successfully_start_second_ballot() throws Exception {
//        // given
//
//        // when + then
//        mockMvc.perform(post("/voting/secondBallot"))
//            .andExpect(status().isOk())
//            .andExpect(handler().methodName("secondBallot"))
//            .andExpect(content().contentType("text/plain;charset=UTF-8"))
//            .andExpect(view().name("voting/secondBallot"));
//    }
//
//    @Test
//    void should_successfully_show_winners_from_constituencies() throws Exception {
//        // given
//        when(votingService.findWinnersInEachConstituency()).thenReturn(createCandidates());
//        when(votingService.countAverageVotersAge()).thenReturn(40.0);
//        Map<String, BigDecimal> gendersWithPercentage = new HashMap<>();
//        gendersWithPercentage.put(MALE.name(), BigDecimal.valueOf(60));
//        gendersWithPercentage.put(FEMALE.name(), BigDecimal.valueOf(40));
//        when(votingService.countGenderPercentage()).thenReturn(gendersWithPercentage);
//        Map<ConstituencyDto, Map<CandidateDto, BigDecimal>> candidatesPercentageInConstituencies = new HashMap<>();
//        when(votingService.groupCandidatesWithPercentageResultsInConstituencies()).thenReturn(candidatesPercentageInConstituencies);
//
//        // when + then
//        mockMvc.perform(post("/voting/result"))
//            .andExpect(status().isOk())
//            .andExpect(handler().methodName("showWinnersFromEachConstituency"))
//            .andExpect(content().contentType("text/plain;charset=UTF-8"))
//            .andExpect(view().name("voting/result"));
//    }
}