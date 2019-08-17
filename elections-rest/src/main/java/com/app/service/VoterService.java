package com.app.service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.app.exceptions.MyException;
import com.app.model.Constituency;
import com.app.model.Voter;
import com.app.model.dto.ModelMapper;
import com.app.model.dto.VoterDto;
import com.app.repository.TokenRepository;
import com.app.repository.VoterRepository;

import lombok.RequiredArgsConstructor;

import static com.app.model.dto.ModelMapper.*;

@Service
@Transactional
@RequiredArgsConstructor
public class VoterService {

    private final VoterRepository voterRepository;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final ToolsService toolsService;

    public VoterDto addVoter(VoterDto voterDto) {
        try {
            Optional.ofNullable(voterDto).orElseThrow(() -> new MyException("VoterDto is null"));
            tokenService.addToken(voterDto);

            return voterDto;
        } catch (Exception e) {
            throw new MyException("Failed to add new voter");
        }
    }

    public VoterDto updateVoter(VoterDto voterDto) {
        try {
            Optional.ofNullable(voterDto).orElseThrow(() -> new MyException("VoterDto is null"));
            Constituency constituency = toolsService.findConstituencyByIdWithErrorCheck(voterDto.getConstituencyDto().getId());
            Voter voter = createVoter(voterDto, constituency);

            return fromVoterToVoterDto(voterRepository.save(voter));
        } catch (Exception e) {
            throw new MyException("Failed to update voter");
        }
    }

    public VoterDto deleteVoter(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("Voter id is null"));
            Voter voter = toolsService.findVoterByIdWithErrorCheck(id);
            voterRepository.deleteById(id);

            return fromVoterToVoterDto(voter);
        } catch (Exception e) {
            throw new MyException("Failed to delete voter by id " + id);
        }
    }

    public VoterDto getOneVoter(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("Voter id is null"));

            return fromVoterToVoterDto(toolsService.findVoterByIdWithErrorCheck(id));
        } catch (Exception e) {
            throw new MyException("Failed to get one voter by id " + id);
        }
    }

    public List<VoterDto> getAllVoters() {
        try {
            return voterRepository.findAll()
                .stream()
                .filter(voter -> Objects.nonNull(voter.getAge()))
                .sorted(Comparator.comparing(Voter::getAge))
                .map(ModelMapper::fromVoterToVoterDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException("Failed to get all voters");
        }
    }

    public String deleteAllVotersWithTokens() {
        try {
            tokenRepository.deleteAll();
            voterRepository.deleteAll();

            return "Deleted all voters with tokens";
        } catch (Exception e) {
            throw new MyException("Failed to delete all voters with tokens");
        }
    }

    private Voter createVoter(final VoterDto voterDto, final Constituency constituency) {
        Voter voter = toolsService.findVoterByIdWithErrorCheck(voterDto.getId());
        if (!voter.getConstituency().getId().equals(constituency.getId())) {
            voter.setConstituency(constituency);
        }
        voter.setAge(voterDto.getAge());
        voter.setEducation(voterDto.getEducation());
        voter.setGender(voterDto.getGender());

        return voter;
    }
}
