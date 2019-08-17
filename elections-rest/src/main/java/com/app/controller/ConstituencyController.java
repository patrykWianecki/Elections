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

import com.app.model.dto.ConstituencyDto;
import com.app.service.ConstituencyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/constituencies")
public class ConstituencyController {

    private final ConstituencyService constituencyService;

    @PostMapping
    public ResponseEntity<ConstituencyDto> addConstituency(@RequestBody ConstituencyDto constituencyDto) {
        return ResponseEntity.ok(constituencyService.addConstituency(constituencyDto));
    }

    @PutMapping
    public ResponseEntity<ConstituencyDto> updateConstituency(@RequestBody ConstituencyDto constituencyDto) {
        return ResponseEntity.ok(constituencyService.updateConstituency(constituencyDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ConstituencyDto> deleteConstituency(@PathVariable Long id) {
        return ResponseEntity.ok(constituencyService.deleteConstituency(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConstituencyDto> getOneConstituency(@PathVariable Long id) {
        return ResponseEntity.ok(constituencyService.getOneConstituency(id));
    }

    @GetMapping
    public ResponseEntity<List<ConstituencyDto>> getAllConstituencies() {
        return ResponseEntity.ok(constituencyService.getAllConstituencies());
    }
}
