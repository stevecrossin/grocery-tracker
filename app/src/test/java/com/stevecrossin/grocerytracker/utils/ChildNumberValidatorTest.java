package com.stevecrossin.grocerytracker.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ChildNumberValidatorTest {
    @Test
    public void isChildNumberValid_ValidAdultNumber_NullErrorReturned() {
        String actualError = InputValidator.isChildNumberValid("1");
        assertNull(actualError);
    }

    @Test
    public void isAdultNumberValid_AdultNumberEmpty_AdultNumberRequiredErrorReturned() {
        String actualError = InputValidator.isChildNumberValid("");
        assertEquals(InputValidator.EMPTY_CHILD_NUMBER_ERROR,actualError);
    }
}