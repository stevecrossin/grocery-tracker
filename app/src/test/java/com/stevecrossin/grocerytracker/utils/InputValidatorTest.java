package com.stevecrossin.grocerytracker.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class InputValidatorTest {

    //region AdultNumberValidator

    @Test
    public void isAdultNumberValid_ValidAdultNumber_NullErrorReturned() {
        String actualError = InputValidator.isAdultNumberValid("1");
        assertNull(actualError);
    }

    @Test
    public void isAdultNumberValid_DecimalAdultNumber_NullErrorReturned() {
        String actualError = InputValidator.isAdultNumberValid("1.5");
        assertEquals(InputValidator.INTEGER_TYPE_ERROR,actualError);
    }

    @Test
    public void isAdultNumberValid_AdultNumberEmpty_AdultNumberRequiredErrorReturned() {
        String actualError = InputValidator.isAdultNumberValid("");
        assertEquals(InputValidator.EMPTY_ADULT_NUMBER_ERROR,actualError);
    }

    @Test
    public void isAdultNumberValid_AdultNumberEquals0_AdultNumberRequiredErrorReturned() {
        String actualError = InputValidator.isAdultNumberValid("0");
        assertEquals(InputValidator.ADULT_NUMBER_ERROR,actualError);
    }

    //endregion

    //region AgeValidator

    @Test
    public void isAgeValid_validAge_NullErrorReturned() {
        String actualError = InputValidator.isAgeValid("20");
        assertNull(actualError);
    }

    @Test
    public void isAgeValid_AgeLessThan18_AgeRangeErrorReturned() {
        String actualError = InputValidator.isAgeValid("16");
        assertEquals(InputValidator.RANGE_AGE_ERROR,actualError);
    }

    @Test
    public void isAgeValid_DecimalAge_AgeRangeErrorReturned() {
        String actualError = InputValidator.isAgeValid("25.5");
        assertEquals(InputValidator.INTEGER_TYPE_ERROR,actualError);
    }

    @Test
    public void isAgeValid_AgeEquals18_AgeRangeErrorReturned() {
        String actualError = InputValidator.isAgeValid("18");
        assertNull(actualError);
    }

    @Test
    public void isAgeValid_AgeMoreThan150_AgeRangeErrorReturned() {
        String actualError = InputValidator.isAgeValid("152");
        assertEquals(InputValidator.RANGE_AGE_ERROR,actualError);
    }

    @Test
    public void isAgeValid_AgeEquals150_AgeRangeErrorReturned() {
        String actualError = InputValidator.isAgeValid("150");
        assertNull(actualError);
    }

    @Test
    public void isAgeValid_AgeEmpty_AgeRequiredErrorReturned() {
        String actualError = InputValidator.isAgeValid("");
        assertEquals(InputValidator.EMPTY_AGE_ERROR,actualError);
    }

    //endregion

    //region ChildNumberValidator

    @Test
    public void isChildNumberValid_ValidChildNumber_NullErrorReturned() {
        String actualError = InputValidator.isChildNumberValid("1");
        assertNull(actualError);
    }

    @Test
    public void isChildNumberValid_ChildNumberEmpty_ChildNumberRequiredErrorReturned() {
        String actualError = InputValidator.isChildNumberValid("");
        assertEquals(InputValidator.EMPTY_CHILD_NUMBER_ERROR,actualError);
    }

    @Test
    public void isChildNumberValid_DecimalChildNumber_ChildNumberRequiredErrorReturned() {
        String actualError = InputValidator.isChildNumberValid("1.5");
        assertEquals(InputValidator.INTEGER_TYPE_ERROR,actualError);
    }

    //endregion

    //region EmailValidator

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

    //endregion

    //region GenderValidator

    @Test
    public void isGenderValid_ValidGender_NullErrorReturned() {
        String actualError = InputValidator.isGenderValid("1");
        assertNull(actualError);
    }

    @Test
    public void isGenderValid_GenderEmpty_GenderRequiredErrorReturned() {
        String actualError = InputValidator.isGenderValid("");
        assertEquals(InputValidator.EMPTY_GENDER_ERROR,actualError);
    }

    //endregion

    //region HeightValidator

    @Test
    public void isHeightValid_ValidHeight_NullErrorReturned() {
        String actualError = InputValidator.isHeightValid("145");
        assertNull(actualError);
    }

    @Test
    public void isHeightValid_HeightMoreThan3Numbers_HeightFormatErrorReturned() {
        String actualError = InputValidator.isHeightValid("1234567");
        assertEquals(InputValidator.FORMAT_HEIGHT_ERROR,actualError);
    }

    @Test
    public void isHeightValid_DecimalHeight_HeightFormatErrorReturned() {
        String actualError = InputValidator.isHeightValid("156.6");
        assertEquals(InputValidator.INTEGER_TYPE_ERROR,actualError);
    }

    @Test
    public void isHeightValid_HeightEmpty_HeightRequiredErrorReturned() {
        String actualError = InputValidator.isHeightValid("");
        assertEquals(InputValidator.EMPTY_HEIGHT_ERROR,actualError);
    }

    //endregion

    //region NameValidator

    @Test
    public void isNameValid_ValidName_NullErrorReturned() {
        String actualError = InputValidator.isNameValid("John A");
        assertNull(actualError);
    }

    @Test
    public void isNameValid_ValidNameWithHyphens_NullErrorReturned() {
        String actualError = InputValidator.isNameValid("John-A");
        assertNull(actualError);
    }

    @Test
    public void isNameValid_ValidNameWithApostrophes_NullErrorReturned() {
        String actualError = InputValidator.isNameValid("John' A");
        assertNull(actualError);
    }

    @Test
    public void isNameValid_NameWithNumbersAndCharacters_NameFormatErrorReturned() {
        String actualError = InputValidator.isNameValid("John 123");
        assertEquals(InputValidator.FORMAT_NAME_ERROR,actualError);
    }

    @Test
    public void isNameValid_NameWithSpecialCharacters_NameFormatErrorReturned() {
        String actualError = InputValidator.isNameValid("John@!");
        assertEquals(InputValidator.FORMAT_NAME_ERROR,actualError);
    }

    @Test
    public void isNameValid_NameEmpty_NameRequiredErrorReturned() {
        String actualError = InputValidator.isNameValid("");
        assertEquals(InputValidator.EMPTY_NAME_ERROR,actualError);
    }

    //endregion

    //region PasswordValidator

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

    //endregion

    //region PostcodeValidator

    @Test
    public void isPostcodeValid_ValidPostcode_NullErrorReturned() {
        String actualError = InputValidator.isPostcodeValid("1234");
        assertNull(actualError);
    }

    @Test
    public void isPostcodeValid_PostcodeLessThan4_PostcodeFormatErrorReturned() {
        String actualError = InputValidator.isPostcodeValid("123");
        assertEquals(InputValidator.FORMAT_POSTCODE_ERROR,actualError);
    }

    @Test
    public void isPostcodeValid_PostcodeMoreThan4_PostcodeFormatErrorReturned() {
        String actualError = InputValidator.isPostcodeValid("12345");
        assertEquals(InputValidator.FORMAT_POSTCODE_ERROR,actualError);
    }

    @Test
    public void isPostcodeValid_PostcodeWith4Characters_PostcodeFormatErrorReturned() {
        String actualError = InputValidator.isPostcodeValid("abcd");
        assertEquals(InputValidator.FORMAT_POSTCODE_ERROR,actualError);
    }

    @Test
    public void isPostcodeValid_PostcodeWith4CharactersAndNumbers_PostcodeFormatErrorReturned() {
        String actualError = InputValidator.isPostcodeValid("ab12");
        assertEquals(InputValidator.FORMAT_POSTCODE_ERROR,actualError);
    }

    @Test
    public void isPostcodeValid_PostcodeEmpty_PostcodeRequiredErrorReturned() {
        String actualError = InputValidator.isPostcodeValid("");
        assertEquals(InputValidator.EMPTY_POSTCODE_ERROR,actualError);
    }

    //endregion

    //region ShopFrequencyValidator

    @Test
    public void isShopFrequencyValid_ValidShopFrequency_NullErrorReturned() {
        String actualError = InputValidator.isShopFrequencyValid("1");
        assertNull(actualError);
    }

    @Test
    public void isShopFrequencyValid_ShopFrequencyEmpty_ShopFrequencyRequiredErrorReturned() {
        String actualError = InputValidator.isShopFrequencyValid("");
        assertEquals(InputValidator.EMPTY_SHOP_FREQUENCY_ERROR,actualError);
    }

    //endregion

    //region TotalFamilyNumberValidator

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
    public void isTotalFamilyNumberValid_DecimalTotalNumberWithAdultAndChildrenNumberEmpty_TotalNumberRequiredErrorReturned() {
        String actualError = InputValidator.isFamilyNumberValid("2.5","","");
        assertEquals(InputValidator.INTEGER_TYPE_ERROR,actualError);
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

    //endregion

    //region WeightValidator
    @Test
    public void isWeightValid_ValidWeight_NullErrorReturned() {
        String actualError = InputValidator.isWeightValid("145");
        assertNull(actualError);
    }

    @Test
    public void isWeightValid_DecimalWeight_WeightFormatErrorReturned() {
        String actualError = InputValidator.isWeightValid("123.4");
        assertEquals(InputValidator.INTEGER_TYPE_ERROR,actualError);
    }

    @Test
    public void isWeightValid_WeightMoreThan3Numbers_WeightFormatErrorReturned() {
        String actualError = InputValidator.isWeightValid("1234567");
        assertEquals(InputValidator.FORMAT_WEIGHT_ERROR,actualError);
    }

    @Test
    public void isWeightValid_WeightEmpty_WeightRequiredErrorReturned() {
        String actualError = InputValidator.isWeightValid("");
        assertEquals(InputValidator.EMPTY_WEIGHT_ERROR,actualError);
    }
    //endregion
}