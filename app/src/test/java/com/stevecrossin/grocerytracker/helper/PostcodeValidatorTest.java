package com.stevecrossin.grocerytracker.helper;

import com.stevecrossin.grocerytracker.utils.InputValidator;

import org.junit.Test;

import static org.junit.Assert.*;

public class PostcodeValidatorTest {


    /** test for the Postcode field
     * Requirement: Exactly 4 number digits
     */
    @Test
    public void IsPostcodeValid_Empty_False(){
        assertNotNull(InputValidator.isPostcodeValid(""));
    }

    @Test
    public void IsPostcodeValid_Correct(){
        assertNull(InputValidator.isPostcodeValid("1234"));
    }

    @Test
    public void IsPostcodeValid_WithCharacters_False(){
        assertNotNull((InputValidator.isPostcodeValid("abcd")));
    }

    @Test
    public void IsPostcodeValid_DigitsAndCharacters_False(){
        assertNotNull((InputValidator.isPostcodeValid("12cd")));
    }

    @Test
    public void IsPostcodeValid_MoreThanFour_False(){
        assertNotNull((InputValidator.isPostcodeValid("123456")));
    }

    @Test
    public void IsPostcodeValid_MoreThanFour_WithCharacters_False() {
        assertNotNull((InputValidator.isPostcodeValid("123avb")));
    }

}