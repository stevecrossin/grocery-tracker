package com.stevecrossin.grocerytracker.utils;

import com.google.android.gms.common.util.Strings;

import java.util.regex.Pattern;

public class InputValidator {

    public static final String EMAIL_ADDRESS = "[a-zA-Z0-9+._%\\-]{1,256}" + "@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";
    public static final String NAME = "^[\\p{L} .'-]+$";
    public static final String POSTCODE_NUMBER = "\\d{4}";
    public static final String INTEGER_TYPE = "^([1-9]\\d*|0)$";

    public static final String INTEGER_TYPE_ERROR = "Value must be a whole number ";
    public static final String EMPTY_NAME_ERROR = "Name is required";
    public static final String FORMAT_NAME_ERROR = "Name is not in correct form ";
    public static final String EMPTY_AGE_ERROR = "Age is required";
    public static final String RANGE_AGE_ERROR = "Age is not eligible for creating account";
    public static final String EMPTY_HEIGHT_ERROR = "Height is required";
    public static final String FORMAT_HEIGHT_ERROR = "Height is not in correct form";
    public static final String EMPTY_WEIGHT_ERROR = "Weight is required";
    public static final String FORMAT_WEIGHT_ERROR = "Weight is not in correct form";
    public static final String EMPTY_POSTCODE_ERROR = "Postcode is required";
    public static final String FORMAT_POSTCODE_ERROR = "Postcode is exactly 4 number digits";
    public static final String EMPTY_ADULT_NUMBER_ERROR = "Adult Number is required";
    public static final String ADULT_NUMBER_ERROR = "Adult Number is at least 1";
    public static final String EMPTY_CHILD_NUMBER_ERROR = "Children Number is required";
    public static final String EMPTY_FAMILY_NUMBER_ERROR = "Family Number is required";
    public static final String EMPTY_EMAIL_ERROR = "Email is required";
    public static final String FORMAT_EMAIL_ERROR = "Email is not in correct format";
    public static final String EMPTY_PASSWORD_ERROR = "Password is required";
    public static final String LENGTH_PASSWORD_ERROR = "Password need to be longer than 5 characters";
    public static final String EMPTY_GENDER_ERROR = "Gender is required";
    public static final String EMPTY_SHOP_FREQUENCY_ERROR = "Shopping Frequency is required";
    public static final String TOTAL_FAMILY_NUMBER_ERROR = "The family number does not matches with the adult and children number";
    public static final String TOTAL_FAMILY_NUMBER_EXCEED_ERROR = "The total number can not smaller than adult/children number";


    public static boolean IsNotEmpty(String field) {
        return (!Strings.isEmptyOrWhitespace(field));
    }

    public static boolean IsMatchedEmailPattern(String field) {
        return Pattern.compile(EMAIL_ADDRESS).matcher(field).matches();
    }

    public static boolean IsMatchedIntegerPattern(String field) {
        return Pattern.compile(INTEGER_TYPE).matcher(field).matches();
    }

    public static boolean ISMatchedNamePattern(String field) {
        return Pattern.compile(NAME).matcher(field).matches();
    }

    public static boolean IsInRange(String field, int start, int end) {
        int target = Integer.parseInt(field);
        return (target >= start && target <= end);
    }

    public static boolean IsExactlyFourNumberDigit(String field) {
        return Pattern.compile(POSTCODE_NUMBER).matcher(field).matches();
    }

    public static boolean IsSumValid(String total, String sub1, String sub2) {
        return (Integer.parseInt(total) == Integer.parseInt(sub1) + Integer.parseInt(sub2));
    }

    public static String isPostcodeValid(String field) {
        if (!IsNotEmpty(field))
            return EMPTY_POSTCODE_ERROR;
        if (!IsExactlyFourNumberDigit(field))
            return FORMAT_POSTCODE_ERROR;
        return null;
    }

    public static String isNameValid(String field) {
        if (!IsNotEmpty(field))
            return EMPTY_NAME_ERROR;
        if (!ISMatchedNamePattern(field))
            return FORMAT_NAME_ERROR;
        return null;
    }

    public static String isAgeValid(String field) {
        if (!IsNotEmpty(field))
            return EMPTY_AGE_ERROR;
        if (!IsMatchedIntegerPattern(field))
            return INTEGER_TYPE_ERROR;
        if (!IsInRange(field, 18, 150))
            return RANGE_AGE_ERROR;
        return null;
    }

    public static String isWeightValid(String field) {
        if (!IsNotEmpty(field))
            return EMPTY_WEIGHT_ERROR;
        if (!IsMatchedIntegerPattern(field))
            return INTEGER_TYPE_ERROR;
        if (field.length() > 3)
            return FORMAT_WEIGHT_ERROR;
        return null;
    }

    public static String isHeightValid(String field) {
        if (!IsNotEmpty(field))
            return EMPTY_HEIGHT_ERROR;
        if (!IsMatchedIntegerPattern(field))
            return INTEGER_TYPE_ERROR;
        if (field.length() > 3)
            return FORMAT_HEIGHT_ERROR;
        return null;
    }

    public static String isGenderValid(String field) {
        if (!IsNotEmpty(field))
            return EMPTY_GENDER_ERROR;
        return null;
    }

    public static String isShopFrequencyValid(String field) {
        if (!IsNotEmpty(field))
            return EMPTY_SHOP_FREQUENCY_ERROR;
        return null;
    }

    public static String isFamilyNumberValid(String totalNumberText, String adultNumberText, String childrenNumberText) {
        if (!IsNotEmpty(totalNumberText))
            return EMPTY_FAMILY_NUMBER_ERROR;
        if (!IsMatchedIntegerPattern(totalNumberText))
            return INTEGER_TYPE_ERROR;
        if (IsNotEmpty(adultNumberText) && !IsInRange(adultNumberText, 0, Integer.parseInt(totalNumberText)))
            return TOTAL_FAMILY_NUMBER_EXCEED_ERROR;
        if (IsNotEmpty(childrenNumberText) && !IsInRange(childrenNumberText, 0, Integer.parseInt(totalNumberText)))
            return TOTAL_FAMILY_NUMBER_EXCEED_ERROR;
        if (IsNotEmpty(adultNumberText) && IsNotEmpty(childrenNumberText) && !IsSumValid(totalNumberText, adultNumberText, childrenNumberText))
            return TOTAL_FAMILY_NUMBER_ERROR;
        return null;
    }

    public static String isAdultNumberValid(String adultNumberText) {
        if (!IsNotEmpty(adultNumberText))
            return EMPTY_ADULT_NUMBER_ERROR;
        if (!IsMatchedIntegerPattern(adultNumberText))
            return INTEGER_TYPE_ERROR;
        if (Integer.parseInt(adultNumberText) < 1)
            return ADULT_NUMBER_ERROR;
        return null;
    }

    public static String isChildNumberValid(String field) {
        if (!IsNotEmpty(field))
            return EMPTY_CHILD_NUMBER_ERROR;
        if (!IsMatchedIntegerPattern(field))
            return INTEGER_TYPE_ERROR;
        return null;
    }


    public static String isEmailValid(String field) {
        if (!IsNotEmpty(field))
            return EMPTY_EMAIL_ERROR;
        if (!IsMatchedEmailPattern(field))
            return FORMAT_EMAIL_ERROR;
        return null;
    }

    public static String isPasswordValid(String field) {
        if (!IsNotEmpty(field))
            return EMPTY_PASSWORD_ERROR;
        if (!IsInRange(Integer.toString(field.length()), 6, 150))
            return LENGTH_PASSWORD_ERROR;
        return null;
    }

}
