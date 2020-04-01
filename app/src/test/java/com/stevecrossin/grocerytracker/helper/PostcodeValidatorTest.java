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
        assertTrue(validator.IsExactlyFourNumberDigit("1234"));
    }

    @Test
    public void IsExactlyForNumberDigit_WithCharacters_False(){
        assertFalse((validator.IsExactlyFourNumberDigit("abcd")));
    }

    @Test
    public void IsExactlyForNumberDigit_DigitsAndCharacters_False(){
        assertFalse((validator.IsExactlyFourNumberDigit("12cd")));
    }

    @Test
    public void IsExactlyForNumberDigit_MoreThanFour_False(){
        assertFalse((validator.IsExactlyFourNumberDigit("123456")));
    }

    @Test
    public void IsExactlyForNumberDigit_MoreThanFour_WithCharacters_False() {
        assertFalse((validator.IsExactlyFourNumberDigit("123avb")));
    }


}