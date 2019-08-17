package com.app.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.app.exceptions.MyException;
import com.app.model.dto.VoterDto;

import static com.app.builders.MockDataForTests.*;
import static org.junit.jupiter.api.Assertions.*;

class VoterValidatorTest {

    private static VoterValidator voterValidator = new VoterValidator();

    @BeforeEach
    void setUp() {
        assertTrue(voterValidator.supports(VoterDto.class));
    }

    @Test
    void should_successfully_valid_voter() {
        // given
        VoterDto validVoter = createValidVoter();
        Errors errors = new BeanPropertyBindingResult(validVoter, "validVoter");

        // when
        voterValidator.validate(validVoter, errors);

        // then
        assertFalse(errors.hasErrors());
    }

    @Test
    void should_find_errors_during_validating_voter() {
        // given
        VoterDto invalidVoter = createInvalidVoter();
        Errors errors = new BeanPropertyBindingResult(invalidVoter, "invalidVoter");

        // when
        voterValidator.validate(invalidVoter, errors);

        // then
        assertTrue(errors.hasErrors());

        FieldError age = errors.getFieldError("age");
        assertNotNull(age);
        assertEquals("Age must be greater or equal to 18", age.getCode());

        FieldError gender = errors.getFieldError("gender");
        assertNotNull(gender);
        assertEquals("Gender must be chosen", gender.getCode());

        FieldError education = errors.getFieldError("education");
        assertNotNull(education);
        assertEquals("Education must be chosen", education.getCode());

        FieldError constituency = errors.getFieldError("constituencyDto");
        assertNotNull(constituency);
        assertEquals("Constituency must be chosen", constituency.getCode());
    }

    @Test
    void should_throw_exception_when_voter_is_null() {
        // given

        // when + then
        assertThrows(MyException.class, () -> voterValidator.validate(null, null));
    }
}