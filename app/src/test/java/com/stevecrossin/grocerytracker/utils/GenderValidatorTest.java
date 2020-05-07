package com.stevecrossin.grocerytracker.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class GenderValidatorTest {

    @Test
    public void isGenderValid_ValidGender_NullErrorReturned() {
        String actualError = InputValidator.isGenderValid("1");
        assertNull(actualError);
    }

    @Test
    public void isGenderValid_GenderEmpty_GenderRequiredErrorReturned() {
        String actualError = InputValidator.isGenderValid("");
        assertEquals(InputValidator.EMPTY_GENDER_ERROR,actualError);
    }
}