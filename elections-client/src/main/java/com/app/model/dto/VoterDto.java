package com.app.model.dto;

import com.app.model.Education;
import com.app.model.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoterDto {

    private Long id;
    private Integer age;
    private Gender gender;
    private Education education;
    private ConstituencyDto constituencyDto;
    private TokenDto tokenDto;
}
