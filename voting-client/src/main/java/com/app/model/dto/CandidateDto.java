package com.app.model.dto;

import org.springframework.web.multipart.MultipartFile;

import com.app.model.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidateDto {

    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private Gender gender;
    private String photo;
    private Integer votes;
    private Boolean isValid;
    private MultipartFile multipartFile;
    private ConstituencyDto constituencyDto;
}

