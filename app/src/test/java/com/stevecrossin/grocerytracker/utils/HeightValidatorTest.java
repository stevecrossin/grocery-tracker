package com.stevecrossin.grocerytracker.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class HeightValidatorTest {

    @Test
    public void isHeightValid_ValidHeight_NullErrorReturned() {
        String actualError = InputValidator.isHeightValid("145");
        assertNull(actualError);
    }

    @Test
    public void isHeightValid_HeightMoreThan3Numbers_HeightFormatErrorReturned() {
        String actualError = InputValidator.isHeightValid("1234567");
        assertEquals(InputValidator.FORMAT_HEIGHT_ERROR,actualError);
    }

    @Test
    public void isHeightValid_HeightEmpty_HeightRequiredErrorReturned() {
        String actualError = InputValidator.isHeightValid("");
        assertEquals(InputValidator.EMPTY_HEIGHT_ERROR,actualError);
    }
}