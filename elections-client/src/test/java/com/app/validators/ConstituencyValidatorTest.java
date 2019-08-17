package com.app.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.app.exceptions.MyException;
import com.app.model.dto.ConstituencyDto;

import static com.app.builders.MockDataForTests.*;
import static org.junit.jupiter.api.Assertions.*;

class ConstituencyValidatorTest {

    private static ConstituencyValidator constituencyValidator = new ConstituencyValidator();

    @BeforeEach
    void setUp() {
        assertTrue(constituencyValidator.supports(ConstituencyDto.class));
    }

    @Test
    void should_successfully_valid_candidate() {
        // given
        ConstituencyDto validConstituency = createValidConstituency();
        Errors errors = new BeanPropertyBindingResult(validConstituency, "validConstituency");

        // when
        constituencyValidator.validate(validConstituency, errors);

        // then
        assertFalse(errors.hasErrors());
    }

    @Test
    void should_find_errors_during_validating_candidate() {
        // given
        ConstituencyDto invalidConstituency = createInvalidConstituency();
        Errors errors = new BeanPropertyBindingResult(invalidConstituency, "invalidConstituency");

        // when
        constituencyValidator.validate(invalidConstituency, errors);

        // then
        assertTrue(errors.hasErrors());

        FieldError name = errors.getFieldError("name");
        assertNotNull(name);
        assertEquals("Name must contain only upper case", name.getCode());
    }

    @Test
    void should_throw_exception_when_candidate_is_null() {
        // given

        // when + then
        assertThrows(MyException.class, () -> constituencyValidator.validate(null, null));
    }
}