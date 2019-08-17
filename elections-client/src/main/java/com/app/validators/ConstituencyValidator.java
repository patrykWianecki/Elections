package com.app.validators;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.exceptions.MyException;
import com.app.model.dto.ConstituencyDto;

@Component
public class ConstituencyValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(ConstituencyDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            ConstituencyDto constituencyDto = (ConstituencyDto) target;
            String constituencyName = constituencyDto.getName();

            if (StringUtils.isEmpty(constituencyName) || !isValid(constituencyName)) {
                errors.rejectValue("name", "Name must contain only upper case");
            }
        } catch (Exception e) {
            throw new MyException("Constituency validation exception");
        }
    }

    private static boolean isValid(String text) {
        return text.matches("([A-ZŻŹĆĄŚĘŁÓŃ]+ )*[A-ZŻŹĆĄŚĘŁÓŃ]+");
    }
}
