package com.app.service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.app.exceptions.MyException;
import com.app.model.Constituency;
import com.app.model.Token;
import com.app.model.Voter;
import com.app.model.dto.ModelMapper;
import com.app.model.dto.VoterDto;
import com.app.repository.TokenRepository;

import lombok.RequiredArgsConstructor;

import static com.app.model.dto.ModelMapper.*;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final ToolsService toolsService;

    public void addToken(VoterDto voterDto) throws MyException {
        try {
            Optional.ofNullable(voterDto).orElseThrow(() -> new MyException("VoterDto is null"));
            Constituency constituency = toolsService.findConstituencyByIdWithErrorCheck(voterDto.getConstituencyDto().getId());
            Voter voter = fromVoterDtoToVoter(voterDto);
            voter.setConstituency(constituency);

            tokenRepository.save(createToken(voter));
        } catch (Exception e) {
            throw new MyException("Failed to add new token");
        }
    }

    private Integer returnUniqueToken() {
        int token = generateToken();
        while (checkToken(token)) {
            token = generateToken();
        }
        return token;
    }

    private boolean checkToken(Integer generatedToken) {
        Optional.ofNullable(generatedToken).orElseThrow(() -> new MyException("Generated token is null"));
        return tokenRepository.findAll()
            .stream()
            .filter(token -> Objects.nonNull(token.getVoterToken()))
            .map(Token::getVoterToken)
            .anyMatch(generatedToken::equals);
    }

    private Integer generateToken() {
        return new Random().nextInt(9000) + 1000;
    }

    public String deleteUsedToken(Integer token) {
        try {
            Optional.ofNullable(token).orElseThrow(() -> new MyException("Token is null"));
            tokenRepository.deleteByVoterToken(token);

            return "Successfully deleted token";
        } catch (Exception e) {
            throw new MyException("Failed to delete token");
        }
    }

    public VoterDto findVoterByToken(Integer voterToken) {
        try {
            Optional.ofNullable(voterToken).orElseThrow(() -> new MyException("Voter token is null"));
            return tokenRepository.findAll()
                .stream()
                .filter(token -> Objects.nonNull(token.getVoterToken()))
                .filter(token -> voterToken.equals(token.getVoterToken()))
                .filter(token -> Objects.nonNull(token.getVoter()))
                .map(Token::getVoter)
                .map(ModelMapper::fromVoterToVoterDto)
                .findFirst()
                .orElseThrow(() -> new MyException("Missing voter with token " + voterToken));
        } catch (Exception e) {
            throw new MyException("Failed to find voter by token " + voterToken);
        }
    }

    private Token createToken(final Voter voter) {
        return Token.builder()
            .voter(voter)
            .voterToken(returnUniqueToken())
            .build();
    }
}
