package com.stevecrossin.grocerytracker.helper;

import com.stevecrossin.grocerytracker.utils.InputValidator;

import org.junit.Test;

import static org.junit.Assert.*;

public class EmailValidatorTest {

    /** test for the Email field
     * Requirement: Required and matches email pattern
     */
    @Test
    public void IsEmailValid_Correct(){

        assertNull(InputValidator.isEmailValid("abc@gmail.com"));
    }

    @Test
    public void IsEmailValid_WrongFormat_False(){
        assertNotNull(InputValidator.isEmailValid("abcgmail.com"));
    }


    @Test
    public void IsEmailValid_IsEmpty_False(){
        assertNotNull(InputValidator.isEmailValid(""));
    }

}