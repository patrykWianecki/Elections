package com.app.validators;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.exceptions.MyException;
import com.app.model.Gender;
import com.app.model.dto.CandidateDto;
import com.app.model.dto.ConstituencyDto;

@Component
public class CandidateValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(CandidateDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            CandidateDto candidateDto = (CandidateDto) target;

            Integer candidateAge = candidateDto.getAge();
            String candidateName = candidateDto.getName();
            String candidateSurname = candidateDto.getSurname();
            Gender candidateGender = candidateDto.getGender();
            ConstituencyDto constituencyDto = candidateDto.getConstituencyDto();

            if (candidateAge < 18) {
                errors.rejectValue("age", "Age must be greater or equal to 18");
            }
            if (StringUtils.isEmpty(candidateName) || !isInputTextValid(candidateDto.getName())) {
                errors.rejectValue("name", "Name must contain only upper case");
            }
            if (StringUtils.isEmpty(candidateSurname) || !isInputTextValid(candidateDto.getSurname())) {
                errors.rejectValue("surname", "Surname must contain only upper case");
            }
            if (candidateGender == null) {
                errors.rejectValue("gender", "Gender must be chosen");
            }
            // if (candidateDto.getMultipartFile().getName() == null) {
            //     errors.rejectValue("photo", "Photo must be added");
            // }
            if (constituencyDto == null || constituencyDto.getId() == null) {
                errors.rejectValue("constituencyDto", "Constituency must be chosen");
            }
        } catch (Exception e) {
            throw new MyException("Candidate validation exception");
        }
    }

    private static boolean isInputTextValid(String text) {
        return text.matches("([A-ZŻŹĆĄŚĘŁÓŃ]+ )*[A-ZŻŹĆĄŚĘŁÓŃ]+");
    }
}
