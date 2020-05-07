package com.stevecrossin.grocerytracker.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class AdultNumberValidatorTest {

    @Test
    public void isAdultNumberValid_ValidAdultNumber_NullErrorReturned() {
        String actualError = InputValidator.isAdultNumberValid("1");
        assertNull(actualError);
    }

    @Test
    public void isAdultNumberValid_AdultNumberEmpty_AdultNumberRequiredErrorReturned() {
        String actualError = InputValidator.isAdultNumberValid("");
        assertEquals(InputValidator.EMPTY_ADULT_NUMBER_ERROR,actualError);
    }

    @Test
    public void isAdultNumberValid_AdultNumberEquals0_AdultNumberRequiredErrorReturned() {
        String actualError = InputValidator.isAdultNumberValid("0");
        assertEquals(InputValidator.ADULT_NUMBER_ERROR,actualError);
    }

}