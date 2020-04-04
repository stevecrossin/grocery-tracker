package com.stevecrossin.grocerytracker.helper;

import com.stevecrossin.grocerytracker.utils.InputValidator;

import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordValidatorTest {

    /** test for the Password field
     * Requirement: Required and More Than 5 characters
     */
    @Test
    public void IsPasswordValid_Correct(){

        assertNull(InputValidator.isPasswordValid("1234567"));
    }

    @Test
    public void IsPasswordValid_TooShort_False(){
        assertNotNull(InputValidator.isPasswordValid("12345"));
    }

    @Test
    public void IsPasswordValid_IsEmpty_False(){

        assertNotNull(InputValidator.isPasswordValid(""));
    }
}