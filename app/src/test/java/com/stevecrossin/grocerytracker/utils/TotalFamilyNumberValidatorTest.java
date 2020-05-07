package com.stevecrossin.grocerytracker.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class TotalFamilyNumberValidatorTest {

    @Test
    public void isTotalFamilyNumberValid_ValidTotalNumberWithValidAdultAndChildrenNumber_NullErrorReturned() {
        String actualError = InputValidator.isFamilyNumberValid("5","2","3");
        assertNull(actualError);
    }

    @Test
    public void isTotalFamilyNumberValid_TotalNumberEmptyWithAdultAndChildrenNumberEmpty_TotalNumberRequiredErrorReturned() {
        String actualError = InputValidator.isFamilyNumberValid("","","");
        assertEquals(InputValidator.EMPTY_FAMILY_NUMBER_ERROR,actualError);
    }

    @Test
    public void isTotalFamilyNumberValid_TotalNumberEmptyWithAdultAndChildrenNumber_TotalNumberRequiredErrorReturned() {
        String actualError = InputValidator.isFamilyNumberValid("","2","3");
        assertEquals(InputValidator.EMPTY_FAMILY_NUMBER_ERROR,actualError);
    }

    @Test
    public void isTotalFamilyNumberValid_TotalNumberWithMissingAdultNumber_NullErrorReturned() {
        String actualError = InputValidator.isFamilyNumberValid("5","","3");
        assertNull(actualError);
    }

    @Test
    public void isTotalFamilyNumberValid_TotalNumberWithMissingChildrenNumber_NullErrorReturned() {
        String actualError = InputValidator.isFamilyNumberValid("5","","2");
        assertNull(actualError);
    }

    @Test
    public void isTotalFamilyNumberValid_TotalNumberWithGreaterAdultNumber_TotalNumberExceedErrorReturned() {
        String actualError = InputValidator.isFamilyNumberValid("5","7","1");
        assertEquals(InputValidator.TOTAL_FAMILY_NUMBER_EXCEED_ERROR,actualError);
    }

    @Test
    public void isTotalFamilyNumberValid_TotalNumberWithGreaterChildrenNumber_TotalNumberExceedErrorReturned() {
        String actualError = InputValidator.isFamilyNumberValid("5","2","7");
        assertEquals(InputValidator.TOTAL_FAMILY_NUMBER_EXCEED_ERROR,actualError);
    }

    @Test
    public void isTotalFamilyNumberValid_TotalNumberWithGreaterSumOfAdultAndChildrenNumber_TotalNumberErrorReturned() {
        String actualError = InputValidator.isFamilyNumberValid("5","4","3");
        assertEquals(InputValidator.TOTAL_FAMILY_NUMBER_ERROR,actualError);
    }
}