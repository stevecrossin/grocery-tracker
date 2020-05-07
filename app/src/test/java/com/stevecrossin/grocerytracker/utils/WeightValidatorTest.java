package com.stevecrossin.grocerytracker.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class WeightValidatorTest {

    @Test
    public void isWeightValid_ValidWeight_NullErrorReturned() {
        String actualError = InputValidator.isWeightValid("145");
        assertNull(actualError);
    }

    @Test
    public void isWeightValid_WeightMoreThan3Numbers_WeightFormatErrorReturned() {
        String actualError = InputValidator.isWeightValid("1234567");
        assertEquals(InputValidator.FORMAT_WEIGHT_ERROR,actualError);
    }

    @Test
    public void isWeightValid_WeightEmpty_WeightRequiredErrorReturned() {
        String actualError = InputValidator.isWeightValid("");
        assertEquals(InputValidator.EMPTY_WEIGHT_ERROR,actualError);
    }
}