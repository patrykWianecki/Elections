package com.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.dto.VoterDto;
import com.app.service.TokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/votersTokens")
public class TokenController {

    private final TokenService tokenService;

    @DeleteMapping("/{token}")
    public ResponseEntity<String> deleteUsedToken(@PathVariable String token) {
        return ResponseEntity.ok(tokenService.deleteUsedToken(Integer.valueOf(token)));
    }

    @GetMapping("/{voterToken}")
    public ResponseEntity<VoterDto> getVoterByToken(@PathVariable Integer voterToken) {
        return ResponseEntity.ok(tokenService.findVoterByToken(voterToken));
    }
}
