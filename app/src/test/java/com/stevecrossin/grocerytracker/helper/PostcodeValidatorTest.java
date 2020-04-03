package com.stevecrossin.grocerytracker.helper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PostcodeValidatorTest {

    InputValidator validator;

    /** test for the Postcode field
     * Requirement: Exactly 4 number digits
     */
    @Test
    public void IsExactlyFourNumberDigit_Correct(){
        assertTrue(validator.isPostcodeValid("1234")==null);
    }

    @Test
    public void IsExactlyForNumberDigit_WithCharacters_False(){
        assertFalse((validator.isPostcodeValid("abcd"))==null);
    }

    @Test
    public void IsExactlyForNumberDigit_DigitsAndCharacters_False(){
        assertFalse((validator.isPostcodeValid("12cd"))==null);
    }

    @Test
    public void IsExactlyForNumberDigit_MoreThanFour_False(){
        assertFalse((validator.isPostcodeValid("123456"))==null);
    }

    @Test
    public void IsExactlyForNumberDigit_MoreThanFour_WithCharacters_False() {
        assertFalse((validator.isPostcodeValid("123avb"))==null);
    }


}