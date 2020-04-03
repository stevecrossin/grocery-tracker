package com.stevecrossin.grocerytracker.helper;

import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordValidatorTest {
    InputValidator validator;

    /** test for the Password field
     * Requirement: Required and More Than 5 characters
     */
    @Test
    public void IsPasswordValid_Correct(){

        assertTrue(validator.isPasswordValid("1234567")==null);
    }

    @Test
    public void IsPasswordValid_TooShort_False(){
        assertFalse(validator.isPasswordValid("12345")==null);
    }

    @Test
    public void IsPasswordValid_IsEmpty_False(){

        assertFalse(validator.isPasswordValid("")==null);
    }
}