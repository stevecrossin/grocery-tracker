package com.stevecrossin.grocerytracker.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class PostcodeValidatorTest {

    @Test
    public void isPostcodeValid_ValidPostcode_NullErrorReturned() {
        String actualError = InputValidator.isPostcodeValid("1234");
        assertNull(actualError);
    }

    @Test
    public void isPostcodeValid_PostcodeLessThan4_PostcodeFormatErrorReturned() {
        String actualError = InputValidator.isPostcodeValid("123");
        assertEquals(InputValidator.FORMAT_POSTCODE_ERROR,actualError);
    }

    @Test
    public void isPostcodeValid_PostcodeMoreThan4_PostcodeFormatErrorReturned() {
        String actualError = InputValidator.isPostcodeValid("12345");
        assertEquals(InputValidator.FORMAT_POSTCODE_ERROR,actualError);
    }

    @Test
    public void isPostcodeValid_PostcodeWith4Characters_PostcodeFormatErrorReturned() {
        String actualError = InputValidator.isPostcodeValid("abcd");
        assertEquals(InputValidator.FORMAT_POSTCODE_ERROR,actualError);
    }

    @Test
    public void isPostcodeValid_PostcodeWith4CharactersAndNumbers_PostcodeFormatErrorReturned() {
        String actualError = InputValidator.isPostcodeValid("ab12");
        assertEquals(InputValidator.FORMAT_POSTCODE_ERROR,actualError);
    }

    @Test
    public void isPostcodeValid_PostcodeEmpty_PostcodeRequiredErrorReturned() {
        String actualError = InputValidator.isPostcodeValid("");
        assertEquals(InputValidator.EMPTY_POSTCODE_ERROR,actualError);
    }

}