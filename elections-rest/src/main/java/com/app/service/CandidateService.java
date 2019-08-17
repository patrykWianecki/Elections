package com.app.service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.app.exceptions.MyException;
import com.app.model.Candidate;
import com.app.model.Constituency;
import com.app.model.dto.CandidateDto;
import com.app.model.dto.ModelMapper;
import com.app.repository.CandidateRepository;
import com.app.repository.ConstituencyRepository;

import lombok.RequiredArgsConstructor;

import static com.app.model.dto.ModelMapper.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ToolsService toolsService;

    public CandidateDto addCandidate(CandidateDto candidateDto) {
        try {
            Optional.ofNullable(candidateDto).orElseThrow(() -> new MyException("CandidateDto is null"));
            Constituency constituency = toolsService.findConstituencyByIdWithErrorCheck(candidateDto.getConstituencyDto().getId());
            Candidate candidate = fromCandidateDtoToCandidate(candidateDto);
            // String filename = fileManager.addFile(candidateDto.getMultipartFile());
            // candidate.setPhoto(filename);
            candidate.setPhoto("photo");
            candidate.setConstituency(constituency);
            candidate.setVotes(0);
            candidate.setIsValid(true);

            return fromCandidateToCandidateDto(candidateRepository.save(candidate));
        } catch (Exception e) {
            throw new MyException("Failed to add new candidate");
        }
    }

    public CandidateDto updateCandidate(CandidateDto candidateDto) {
        try {
            Optional.ofNullable(candidateDto).orElseThrow(() -> new MyException("CandidateDto is null"));
            Constituency constituency = toolsService.findConstituencyByIdWithErrorCheck(candidateDto.getConstituencyDto().getId());
            Candidate candidate = createCandidate(candidateDto, constituency);

            return fromCandidateToCandidateDto(candidateRepository.save(candidate));
        } catch (Exception e) {
            throw new MyException("Failed to update candidate");
        }
    }

    public CandidateDto deleteCandidate(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("Candidate id is null"));
            Candidate candidate = toolsService.findCandidateByIdWithErrorCheck(id);
            //            fileManager.removeFile(toolsService.findCandidateWithErrorCheck(id).getPhoto());
            candidateRepository.deleteById(id);

            return fromCandidateToCandidateDto(candidate);
        } catch (Exception e) {
            throw new MyException("Failed to delete candidate by id " + id);
        }
    }

    public CandidateDto getOneCandidate(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("Candidate id is null"));

            return fromCandidateToCandidateDto(toolsService.findCandidateByIdWithErrorCheck(id));
        } catch (Exception e) {
            throw new MyException("Failed to get one candidate by id " + id);
        }
    }

    public List<CandidateDto> getAllCandidates() {
        try {
            return candidateRepository.findAll()
                .stream()
                .filter(candidate -> Objects.nonNull(candidate.getConstituency()))
                .filter(candidate -> Objects.nonNull(candidate.getConstituency().getId()))
                .sorted(Comparator.comparing(candidate -> candidate.getConstituency().getId()))
                .map(ModelMapper::fromCandidateToCandidateDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException("Failed to get all candidates");
        }
    }

    private Candidate createCandidate(final CandidateDto candidateDto, final Constituency constituency) {
        Candidate candidate = toolsService.findCandidateByIdWithErrorCheck(candidateDto.getId());
        //            if (!fileManager.updateFile(candidateDto.getMultipartFile(), candidateDto.getPhoto())) {
        //                throw new MyException("FILE UPDATE EXCEPTION");
        //            }
        if (!candidate.getConstituency().getId().equals(constituency.getId())) {
            candidate.setConstituency(constituency);
        }
        candidate.setName(candidateDto.getName());
        candidate.setSurname(candidateDto.getSurname());
        candidate.setAge(candidateDto.getAge());
        candidate.setGender(candidateDto.getGender());
        candidate.setVotes(candidate.getVotes());
        candidate.setIsValid(candidate.getIsValid());
        candidate.setPhoto("photo");
        //            candidate.setPhoto(candidateDto.getPhoto());

        return candidate;
    }
}
