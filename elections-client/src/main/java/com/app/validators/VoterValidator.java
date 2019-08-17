package com.app.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.exceptions.MyException;
import com.app.model.Education;
import com.app.model.Gender;
import com.app.model.dto.ConstituencyDto;
import com.app.model.dto.VoterDto;

@Component
public class VoterValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(VoterDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            VoterDto voterDto = (VoterDto) target;
            Integer voterAge = voterDto.getAge();
            Gender voterGender = voterDto.getGender();
            Education voterEducation = voterDto.getEducation();
            ConstituencyDto constituencyDto = voterDto.getConstituencyDto();

            if (voterAge < 18) {
                errors.rejectValue("age", "Age must be greater or equal to 18");
            }
            if (voterGender == null) {
                errors.rejectValue("gender", "Gender must be chosen");
            }
            if (voterEducation == null) {
                errors.rejectValue("education", "Education must be chosen");
            }
            if (constituencyDto == null || constituencyDto.getId() == null) {
                errors.rejectValue("constituencyDto", "Constituency must be chosen");
            }
        } catch (Exception e) {
            throw new MyException("Voter validation exception");
        }
    }
}
