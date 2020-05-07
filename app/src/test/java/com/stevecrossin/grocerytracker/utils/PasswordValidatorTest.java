package com.stevecrossin.grocerytracker.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordValidatorTest {

    @Test
    public void isPasswordValid_ValidPassword_NullErrorReturned() {
        String actualError = InputValidator.isPasswordValid("1234567");
        assertNull(actualError);
    }

    @Test
    public void isPasswordValid_PasswordLessThan6Characters_LengthPasswordErrorReturned() {
        String actualError = InputValidator.isPasswordValid("12345");
        assertEquals(InputValidator.LENGTH_PASSWORD_ERROR,actualError);
    }

    @Test
    public void isPasswordValid_ValidPasswordWithMixedCharactersAndNumbers_NullErrorReturned() {
        String actualError = InputValidator.isPasswordValid("1a2b3c4v");
        assertNull(actualError);
    }

    @Test
    public void isPasswordValid_ValidPasswordWithOnlySpecialCharacters_NullErrorReturned() {
        String actualError = InputValidator.isPasswordValid("!@#$%^&");
        assertNull(actualError);
    }

    @Test
    public void isPasswordValid_PasswordEmpty_PasswordRequiredErrorReturned() {
        String actualError = InputValidator.isPasswordValid("");
        assertEquals(InputValidator.EMPTY_PASSWORD_ERROR,actualError);
    }

}