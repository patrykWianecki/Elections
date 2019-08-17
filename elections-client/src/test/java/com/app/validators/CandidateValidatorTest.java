package com.app.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.app.exceptions.MyException;
import com.app.model.dto.CandidateDto;

import static com.app.builders.MockDataForTests.*;
import static org.junit.jupiter.api.Assertions.*;

class CandidateValidatorTest {

    private static CandidateValidator candidateValidator = new CandidateValidator();

    @BeforeEach
    void setUp() {
        assertTrue(candidateValidator.supports(CandidateDto.class));
    }

    @Test
    void should_successfully_valid_constituency() {
        // given
        CandidateDto validCandidate = createValidCandidate();
        Errors errors = new BeanPropertyBindingResult(validCandidate, "validCandidate");

        // when
        candidateValidator.validate(validCandidate, errors);

        // then
        assertFalse(errors.hasErrors());
    }

    @Test
    void should_find_errors_during_validating_constituency() {
        // given
        CandidateDto invalidCandidate = createInvalidCandidate();
        Errors errors = new BeanPropertyBindingResult(invalidCandidate, "invalidCandidate");

        // when
        candidateValidator.validate(invalidCandidate, errors);

        // then
        assertTrue(errors.hasErrors());

        FieldError age = errors.getFieldError("age");
        assertNotNull(age);
        assertEquals("Age must be greater or equal to 18", age.getCode());

        FieldError name = errors.getFieldError("name");
        assertNotNull(name);
        assertEquals("Name must contain only upper case", name.getCode());

        FieldError surname = errors.getFieldError("surname");
        assertNotNull(surname);
        assertEquals("Surname must contain only upper case", surname.getCode());

        FieldError gender = errors.getFieldError("gender");
        assertNotNull(gender);
        assertEquals("Gender must be chosen", gender.getCode());

        FieldError constituency = errors.getFieldError("constituencyDto");
        assertNotNull(constituency);
        assertEquals("Constituency must be chosen", constituency.getCode());
    }

    @Test
    void should_throw_exception_when_constituency_is_null() {
        // given

        // when + then
        assertThrows(MyException.class, () -> candidateValidator.validate(null, null));
    }
}