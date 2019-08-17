package com.app.controller;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.app.model.dto.CandidateDto;
import com.app.model.dto.ConstituencyDto;
import com.app.service.CandidateService;
import com.app.service.ConstituencyService;
import com.app.validators.CandidateValidator;

import static com.app.builders.MockDataForTests.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CandidateControllerTest {

    @MockBean
    private CandidateService candidateService;
    @MockBean
    private ConstituencyService constituencyService;
    @MockBean
    private CandidateValidator candidateValidator;

    @Autowired
    private MockMvc mockMvc;

    private CandidateDto candidateDto = createCandidateDto();
    private List<ConstituencyDto> constituencyDto = Collections.singletonList(createConstituencyDto());

    @Test
    void should_successfully_add_candidate() throws Exception {
        // given
        when(candidateValidator.supports(any())).thenReturn(true);
        when(constituencyService.getAllConstituencies()).thenReturn(constituencyDto);

        // when
        mockMvc.perform(get("/candidates/add"))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("addCandidate"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("candidates/add"));
    }

    @Test
    void should_successfully_add_candidate_with_post() throws Exception {
        // given
        when(candidateValidator.supports(any())).thenReturn(true);
        when(candidateService.addCandidate(any())).thenReturn(candidateDto);

        // when
        mockMvc.perform(post("/candidates/add"))
            .andExpect(status().isFound())
            .andExpect(handler().methodName("addCandidatePost"))
            .andExpect(view().name("redirect:/candidates"));
    }

    @Test
    void should_successfully_update_candidate() throws Exception {
        // given
        when(candidateValidator.supports(any())).thenReturn(true);
        when(candidateService.getOneCandidate(anyLong())).thenReturn(candidateDto);
        when(constituencyService.getAllConstituencies()).thenReturn(constituencyDto);

        // when
        mockMvc.perform(get("/candidates/edit/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("editCandidate"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("candidates/edit"));
    }

    @Test
    void should_successfully_update_candidate_with_post() throws Exception {
        // given
        when(candidateValidator.supports(any())).thenReturn(true);
        when(candidateService.updateCandidate(any())).thenReturn(candidateDto);

        // when
        mockMvc.perform(post("/candidates/edit"))
            .andExpect(status().isFound())
            .andExpect(handler().methodName("editCandidatePost"))
            .andExpect(view().name("redirect:/candidates"));
    }

    @Test
    void should_successfully_delete_candidate_by_id() throws Exception {
        // given
        when(candidateService.deleteCandidate(anyLong())).thenReturn(candidateDto);

        // when
        mockMvc.perform(post("/candidates/delete?id={id}", 1))
            .andExpect(status().isFound())
            .andExpect(handler().methodName("deleteCandidate"))
            .andExpect(view().name("redirect:/candidates"));
    }

    @Test
    void should_successfully_find_one_candidate() throws Exception {
        // given
        when(candidateValidator.supports(any())).thenReturn(true);
        when(candidateService.getOneCandidate(anyLong())).thenReturn(candidateDto);

        // when
        mockMvc.perform(get("/candidates/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("showOneCandidate"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("candidates/one"));
    }

//    @Test
//    void should_successfully_find_all_candidates() throws Exception {
//        // given
//        when(candidateService.getAllCandidates()).thenReturn(Collections.singletonList(candidateDto));
//
//        // when
//        mockMvc.perform(get("/candidates"))
//            .andExpect(status().isFound())
//            .andExpect(handler().methodName("showAllCandidates"))
//            .andExpect(content().contentType("text/html;charset=UTF-8"))
//            .andExpect(view().name("candidates/all"));
//    }
}