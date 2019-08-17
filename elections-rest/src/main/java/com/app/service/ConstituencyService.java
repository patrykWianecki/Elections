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
import com.app.model.dto.ConstituencyDto;
import com.app.model.dto.ModelMapper;
import com.app.repository.ConstituencyRepository;

import lombok.RequiredArgsConstructor;

import static com.app.model.dto.ModelMapper.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ConstituencyService {

    private final ConstituencyRepository constituencyRepository;
    private final ToolsService toolsService;

    public ConstituencyDto addConstituency(ConstituencyDto constituencyDto) {
        try {
            Optional.ofNullable(constituencyDto).orElseThrow(() -> new MyException("ConstituencyDto is null"));
            Constituency constituency = fromConstituencyDtoToConstituency(constituencyDto);

            return fromConstituencyToConstituencyDto(constituencyRepository.save(constituency));
        } catch (Exception e) {
            throw new MyException("Failed to add new constituency");
        }
    }

    public ConstituencyDto updateConstituency(ConstituencyDto constituencyDto) {
        try {
            Optional.ofNullable(constituencyDto).orElseThrow(() -> new MyException("ConstituencyDto is null"));
            Constituency constituency = toolsService.findConstituencyByIdWithErrorCheck(constituencyDto.getId());
            constituency.setName(constituencyDto.getName());

            return fromConstituencyToConstituencyDto(constituencyRepository.save(constituency));
        } catch (Exception e) {
            throw new MyException("Failed to update constituency");
        }
    }

    public ConstituencyDto deleteConstituency(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("Constituency id is null"));
            Constituency constituency = toolsService.findConstituencyByIdWithErrorCheck(id);
            constituencyRepository.deleteById(id);

            return fromConstituencyToConstituencyDto(constituency);
        } catch (Exception e) {
            throw new MyException("Failed to delete constituency by id " + id);
        }
    }

    public ConstituencyDto getOneConstituency(Long id) {
        try {
            Optional.ofNullable(id).orElseThrow(() -> new MyException("Constituency id is null"));
            Constituency constituency = toolsService.findConstituencyByIdWithErrorCheck(id);

            return fromConstituencyToConstituencyDto(constituency);
        } catch (Exception e) {
            throw new MyException("Failed to get one constituency by id " + id);
        }
    }

    public List<ConstituencyDto> getAllConstituencies() {
        try {
            return constituencyRepository.findAll()
                .stream()
                .filter(constituency -> Objects.nonNull(constituency.getId()))
                .sorted(Comparator.comparing(Constituency::getId))
                .map(ModelMapper::fromConstituencyToConstituencyDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException("Failed to get all constituencies");
        }
    }
}
