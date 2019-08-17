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

import com.app.model.dto.ConstituencyDto;
import com.app.model.dto.VoterDto;
import com.app.service.ConstituencyService;
import com.app.service.VoterService;
import com.app.validators.VoterValidator;

import static com.app.builders.MockDataForTests.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class VoterControllerTest {

    @MockBean
    private VoterService voterService;
    @MockBean
    private ConstituencyService constituencyService;
    @MockBean
    private VoterValidator voterValidator;

    @Autowired
    private MockMvc mockMvc;

    private VoterDto voterDto = createVoterDto();
    private List<ConstituencyDto> constituencyDtos = Collections.singletonList(createConstituencyDto());

    @Test
    void should_successfully_add_voter() throws Exception {
        // given
        when(voterValidator.supports(any())).thenReturn(true);
        when(voterService.addVoter(any())).thenReturn(voterDto);
        when(constituencyService.getAllConstituencies()).thenReturn(constituencyDtos);

        // when
        mockMvc.perform(get("/voters/add"))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("addVoter"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("voters/add"));
    }

    @Test
    void should_successfully_add_voter_with_post() throws Exception {
        // given
        when(voterValidator.supports(any())).thenReturn(true);
        when(voterService.addVoter(any())).thenReturn(voterDto);

        // when
        mockMvc.perform(post("/voters/add"))
            .andExpect(status().isFound())
            .andExpect(handler().methodName("addVoterPost"))
            .andExpect(view().name("redirect:/voters"));
    }

    @Test
    void should_successfully_update_voter() throws Exception {
        // given
        when(voterValidator.supports(any())).thenReturn(true);
        when(voterService.getOneVoter(any())).thenReturn(voterDto);
        when(constituencyService.getAllConstituencies()).thenReturn(constituencyDtos);

        // when
        mockMvc.perform(get("/voters/edit/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("editVoter"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("voters/edit"));
    }

    @Test
    void should_successfully_update_voter_with_post() throws Exception {
        // given
        when(voterValidator.supports(any())).thenReturn(true);
        when(voterService.updateVoter(any())).thenReturn(voterDto);

        // when
        mockMvc.perform(post("/voters/edit"))
            .andExpect(status().isFound())
            .andExpect(handler().methodName("editVoterPost"))
            .andExpect(view().name("redirect:/voters"));
    }

    @Test
    void should_successfully_delete_voter_by_id() throws Exception {
        // given
        when(voterService.deleteVoter(anyLong())).thenReturn(voterDto);

        // when
        mockMvc.perform(post("/voters/delete?id={id}", 1))
            .andExpect(status().isFound())
            .andExpect(handler().methodName("deleteVoter"))
            .andExpect(view().name("redirect:/voters"));
    }

    @Test
    void should_successfully_find_one_voter() throws Exception {
        // given
        when(voterValidator.supports(any())).thenReturn(true);
        when(voterService.getOneVoter(anyLong())).thenReturn(voterDto);

        // when
        mockMvc.perform(get("/voters/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("showOneVoter"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("voters/one"));
    }

    @Test
    void should_successfully_find_all_voters() throws Exception {
        // given
        when(voterService.getAllVoters()).thenReturn(Collections.singletonList(voterDto));

        // when
        mockMvc.perform(get("/voters"))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("showAllVoters"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("voters/all"));
    }
}