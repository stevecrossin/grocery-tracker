package com.stevecrossin.grocerytracker.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AgeValidatorTest {

    @Test
    public void isAgeValid_validAge_NullErrorReturned() {
        String actualError = InputValidator.isAgeValid("20");
        assertNull(actualError);
    }

    @Test
    public void isAgeValid_AgeLessThan18_AgeRangeErrorReturned() {
        String actualError = InputValidator.isAgeValid("16");
        assertEquals(InputValidator.RANGE_AGE_ERROR,actualError);
    }

    @Test
    public void isAgeValid_AgeEquals18_AgeRangeErrorReturned() {
        String actualError = InputValidator.isAgeValid("18");
        assertNull(actualError);
    }

    @Test
    public void isAgeValid_AgeMoreThan150_AgeRangeErrorReturned() {
        String actualError = InputValidator.isAgeValid("152");
        assertEquals(InputValidator.RANGE_AGE_ERROR,actualError);
    }

    @Test
    public void isAgeValid_AgeEquals150_AgeRangeErrorReturned() {
        String actualError = InputValidator.isAgeValid("150");
        assertNull(actualError);
    }

    @Test
    public void isAgeValid_AgeEmpty_AgeRequiredErrorReturned() {
        String actualError = InputValidator.isAgeValid("");
        assertEquals(InputValidator.EMPTY_AGE_ERROR,actualError);
    }

}