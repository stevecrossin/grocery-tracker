package com.stevecrossin.grocerytracker.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class NameValidatorTest {

    @Test
    public void isNameValid_ValidName_NullErrorReturned() {
        String actualError = InputValidator.isNameValid("John A");
        assertNull(actualError);
    }

    @Test
    public void isNameValid_ValidNameWithHyphens_NullErrorReturned() {
        String actualError = InputValidator.isNameValid("John-A");
        assertNull(actualError);
    }

    @Test
    public void isNameValid_ValidNameWithApostrophes_NullErrorReturned() {
        String actualError = InputValidator.isNameValid("John' A");
        assertNull(actualError);
    }

    @Test
    public void isNameValid_NameWithNumbersAndCharacters_NameFormatErrorReturned() {
        String actualError = InputValidator.isNameValid("John 123");
        assertEquals(InputValidator.FORMAT_NAME_ERROR,actualError);
    }

    @Test
    public void isNameValid_NameWithSpecialCharacters_NameFormatErrorReturned() {
        String actualError = InputValidator.isNameValid("John@!");
        assertEquals(InputValidator.FORMAT_NAME_ERROR,actualError);
    }

    @Test
    public void isNameValid_NameEmpty_NameRequiredErrorReturned() {
        String actualError = InputValidator.isNameValid("");
        assertEquals(InputValidator.EMPTY_NAME_ERROR,actualError);
    }
}