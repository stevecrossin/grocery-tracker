package com.stevecrossin.grocerytracker.helper;

import android.text.TextUtils;
import android.util.Patterns;

import com.google.android.gms.common.util.Strings;

import java.util.List;
import java.util.regex.Pattern;

public class InputValidator {

    private static final String POSTCODE_NUMBER = "\\d{4}";
    public static final String EMAIL_ADDRESS
            =
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+";


    public static final boolean IsNotEmpty(String field)
    {
        return (!Strings.isEmptyOrWhitespace(field));
    }

    public static final boolean IsMatchedEmailPattern(String field)
    {
        return  Pattern.compile(EMAIL_ADDRESS).matcher(field).matches();
    }

    public static final boolean IsExactlyFourNumberDigit(String field)
    {
        return Pattern.compile(POSTCODE_NUMBER).matcher(field).matches();
    }

    public static final boolean isPostcodeValid(String field)
    {
        boolean isValid = IsExactlyFourNumberDigit(field);
        return isValid;
    }

    public static final boolean isNameValid(String field)
    {
        boolean isValid = IsNotEmpty(field);
        return isValid;
    }

    public static final boolean isWeightValid(String field)
    {
        boolean isValid = IsNotEmpty(field);
        return isValid;
    }

    public static final boolean isHeightValid(String field)
    {
        boolean isValid = IsNotEmpty(field);
        return isValid;
    }

    public static final boolean isGenderValid(String field)
    {
        boolean isValid = IsNotEmpty(field);
        return isValid;
    }

    public static final boolean isShopFrequencyValid(String field)
    {
        boolean isValid = IsNotEmpty(field);
        return isValid;
    }

    public static final boolean isFamilyNumberValid(String field)
    {
        boolean isValid = IsNotEmpty(field);
        return isValid;
    }

    public static final boolean isAdultNumberValid(String field)
    {
        boolean isValid = IsNotEmpty(field);
        return isValid;
    }

    public static final boolean isChildNumberValid(String field)
    {
        boolean isValid = IsNotEmpty(field);
        return isValid;
    }

    public static final boolean isEmailValid(String field)
    {
        boolean isValid = IsNotEmpty(field) && IsMatchedEmailPattern(field);
        return isValid;
    }

    public static final boolean isPasswordValid(String field)
    {
        boolean isValid = IsNotEmpty(field);
        return isValid;
    }

}
