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

import com.app.model.dto.CandidateDto;
import com.app.service.CandidateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<CandidateDto> addCandidate(@RequestBody CandidateDto candidateDto) {
        return ResponseEntity.ok(candidateService.addCandidate(candidateDto));
    }

    @PutMapping
    public ResponseEntity<CandidateDto> updateCandidate(@RequestBody CandidateDto candidateDto) {
        return ResponseEntity.ok(candidateService.updateCandidate(candidateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CandidateDto> deleteCandidate(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.deleteCandidate(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDto> getOneCandidate(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.getOneCandidate(id));
    }

    @GetMapping
    public ResponseEntity<List<CandidateDto>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }
}
