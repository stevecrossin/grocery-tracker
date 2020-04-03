package com.stevecrossin.grocerytracker.helper;

import android.text.TextUtils;

import org.junit.Test;

import static org.junit.Assert.*;

public class EmailValidatorTest {

    InputValidator validator;

    /** test for the Email field
     * Requirement: Required and matches email pattern
     */
    @Test
    public void IsEmailValid_Correct(){

        assertTrue(validator.isEmailValid("abc@gmail.com")==null);
    }

    @Test
    public void IsEmailValid_WrongFormat_False(){
        assertFalse(validator.isEmailValid("abcgmail.com")==null);
    }


    @Test
    public void IsEmailValid_IsEmpty_False(){
        assertFalse(validator.isEmailValid("")==null);
    }

}