package com.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.dto.VoterDto;
import com.app.service.VoterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voters")
public class VoterController {

    private final VoterService voterService;

    @PostMapping
    public ResponseEntity<VoterDto> addVoter(@RequestBody VoterDto voterDto) {
        return ResponseEntity.ok(voterService.addVoter(voterDto));
    }

    @PutMapping
    public ResponseEntity<VoterDto> updateVoter(@RequestBody VoterDto voterDto) {
        return ResponseEntity.ok(voterService.updateVoter(voterDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VoterDto> deleteVoter(@PathVariable Long id) {
        return ResponseEntity.ok(voterService.deleteVoter(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoterDto> getOneVoter(@PathVariable Long id) {
        return ResponseEntity.ok(voterService.getOneVoter(id));
    }

    @GetMapping
    public ResponseEntity<List<VoterDto>> getAllVoters() {
        return ResponseEntity.ok(voterService.getAllVoters());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllVoters() {
        return ResponseEntity.ok(voterService.deleteAllVotersWithTokens());
    }
}
