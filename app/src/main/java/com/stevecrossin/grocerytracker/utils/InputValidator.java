package com.stevecrossin.grocerytracker.utils;

import com.google.android.gms.common.util.Strings;

import java.util.regex.Pattern;

public class InputValidator {

    private static final String POSTCODE_NUMBER = "\\d{4}";
    private static final int MIN_PASSWORD_LENGTH = 5;
    public static final String EMAIL_ADDRESS
            =
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+";

    private static final String EMPTY_NAME_ERROR = "Name is required";
    private static final String EMPTY_AGE_ERROR = "Age is required";
    private static final String RANGE_AGE_ERROR = "Age is not eligible for creating account";
    private static final String EMPTY_HEIGHT_ERROR = "Height is required";
    private static final String EMPTY_WEIGHT_ERROR = "Weight is required";
    private static final String EMPTY_POSTCODE_ERROR = "Postcode is required";
    private static final String FORMAT_POSTCODE_ERROR = "Postcode is exactly 4 characters";
    private static final String EMPTY_ADULT_NUMBER_ERROR = "Adult Number is required";
    private static final String EMPTY_CHILD_NUMBER_ERROR = "Children Number is required";
    private static final String EMPTY_FAMILY_NUMBER_ERROR = "Family NUmber is required";
    private static final String EMPTY_EMAIL_ERROR = "Email is required";
    private static final String FORMAT_EMAIL_ERROR = "Email is not in correct format";
    private static final String EMPTY_PASSWORD_ERROR = "Password is required";
    private static final String LENGTH_PASSWORD_ERROR = "Password need to be longer than 5 characters";
    private static final String EMPTY_GENDER_ERROR = "Gender is required";
    private static final String EMPTY_SHOP_FREQUENCY_ERROR = "Shopping Frequency is required";
    private static final String TOTAL_FAMILY_NUMBER_ERROR = "The family number does not matches with the adult and children number";


    public static final boolean IsNotEmpty(String field)
    {
        return (!Strings.isEmptyOrWhitespace(field));
    }

    public static final boolean IsLengthGreaterThan(String field, int targetLength)
    {
        return (field.length()>targetLength);
    }

    public static final boolean IsMatchedEmailPattern(String field)
    {
        return  Pattern.compile(EMAIL_ADDRESS).matcher(field).matches();
    }

    public static final boolean IsInRange(String field, int start, int end)
    {
        int target = Integer.parseInt(field);
        return  (target>=start && target <= end);
    }

    public static final boolean IsExactlyFourNumberDigit(String field)
    {
        return Pattern.compile(POSTCODE_NUMBER).matcher(field).matches();
    }

    public static final String isPostcodeValid(String field)
    {
        if  (!IsNotEmpty(field))
            return EMPTY_POSTCODE_ERROR;
        if (!IsExactlyFourNumberDigit(field))
            return FORMAT_POSTCODE_ERROR;
        return null;
    }

    public static final String isNameValid(String field)
    {
        if  (!IsNotEmpty(field))
            return EMPTY_NAME_ERROR;
        return null;
    }

    public static final String isAgeValid(String field)
    {
        if  (!IsNotEmpty(field))
            return EMPTY_AGE_ERROR;
        if (!IsInRange(field,16,150))
            return RANGE_AGE_ERROR;
        return null;
    }

    public static final String isWeightValid(String field)
    {
        if  (!IsNotEmpty(field))
            return EMPTY_WEIGHT_ERROR;
        return null;
    }

    public static final String isHeightValid(String field)
    {
        if  (!IsNotEmpty(field))
            return EMPTY_HEIGHT_ERROR;
        return null;
    }

    public static final String isGenderValid(String field)
    {
        if  (!IsNotEmpty(field))
            return EMPTY_GENDER_ERROR;
        return null;
    }

    public static final String isShopFrequencyValid(String field)
    {
        if  (!IsNotEmpty(field))
            return EMPTY_SHOP_FREQUENCY_ERROR;
        return null;
    }

    public static final String isFamilyNumberValid(String field)
    {
        if  (!IsNotEmpty(field))
            return EMPTY_FAMILY_NUMBER_ERROR;
        return null;
    }

    public static final String isAdultNumberValid(String field)
    {
        if  (!IsNotEmpty(field))
            return EMPTY_ADULT_NUMBER_ERROR;
        return null;
    }

    public static final String isChildNumberValid(String field)
    {
        if  (!IsNotEmpty(field))
            return EMPTY_CHILD_NUMBER_ERROR;
        return null;
    }

    public static final String isTotalFamilyNumberValid(String total, String adult, String children)
    {
        if (Integer.parseInt(total) != Integer.parseInt(adult) +Integer.parseInt(children))
            return TOTAL_FAMILY_NUMBER_ERROR;
        return null;
    }

    public static final String isEmailValid(String field)
    {
        if  (!IsNotEmpty(field))
            return EMPTY_EMAIL_ERROR;
        if  (!IsMatchedEmailPattern(field))
            return FORMAT_EMAIL_ERROR;
        return null;
    }

    public static final String isPasswordValid(String field)
    {
        if  (!IsNotEmpty(field))
            return EMPTY_PASSWORD_ERROR;
        if  (!IsInRange(Integer.toString(field.length()),6,20))
            return LENGTH_PASSWORD_ERROR;
        return null;
    }

}
