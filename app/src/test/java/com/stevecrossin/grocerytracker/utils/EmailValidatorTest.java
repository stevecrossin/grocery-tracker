package com.stevecrossin.grocerytracker.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class EmailValidatorTest {

    @Test
    public void isEmailValid_ValidEmail_NullErrorReturned() {
        String actualError = InputValidator.isEmailValid("abc@g.c");
        assertNull(actualError);
    }

    @Test
    public void isEmailValid_EmailMissesSymbol_EmailFormatErrorReturned() {
        String actualError = InputValidator.isEmailValid("abcg.c");
        assertEquals(InputValidator.FORMAT_EMAIL_ERROR,actualError);
    }

   @Test
    public void isEmailValid_EmailMissesDomain_EmailFormatErrorReturned() {
        String actualError = InputValidator.isEmailValid("abc@c");
        assertEquals(InputValidator.FORMAT_EMAIL_ERROR,actualError);
    }

    @Test
    public void isEmailValid_EmailEmpty_EmailRequiredErrorReturned() {
        String actualError = InputValidator.isEmailValid("");
        assertEquals(InputValidator.EMPTY_EMAIL_ERROR,actualError);
    }

}