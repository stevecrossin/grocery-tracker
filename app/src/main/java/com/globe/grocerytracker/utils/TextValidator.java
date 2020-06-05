package com.globe.grocerytracker.utils;

import android.widget.TextView;

/**
 * Call specific InputValidation for each field and set error for each field based on the error
 */
public class TextValidator {
    private TextView textView;

    public TextValidator(TextView textView) {
        this.textView = textView;
    }

    public void validateName(String nameText) {
        textView.setError(InputValidator.isNameValid(nameText));
    }

    public void validateAge(String ageText) {
        textView.setError(InputValidator.isAgeValid(ageText));
    }

    public void validateHeight(String heightText) {
        textView.setError(InputValidator.isHeightValid(heightText));
    }

    public void validateWeight(String weightText) {
        textView.setError(InputValidator.isWeightValid(weightText));
    }

    public void validateEmail(String emailText) {
        textView.setError(InputValidator.isEmailValid(emailText));
    }

    public void validatePassword(String passwordText) {
        textView.setError(InputValidator.isPasswordValid(passwordText),null);
    }

    public void validateGender(String genderText) {
        textView.setError(InputValidator.isGenderValid(genderText.equals("0") ? "" : genderText));
    }

    public void validatePostcode(String postcodeText) {
        textView.setError(InputValidator.isPostcodeValid(postcodeText));
    }

    public void validateHouseholdNumber(String householdNumberText, String adultNumberText, String childrenNumberText) {
        textView.setError(InputValidator.isFamilyNumberValid(householdNumberText,adultNumberText,childrenNumberText));
    }

    public void validateAdultNumber(String adultNumberText) {
        textView.setError(InputValidator.isAdultNumberValid(adultNumberText));
    }

    public void validateChildNumber(String childNumberText) {
        textView.setError(InputValidator.isChildNumberValid(childNumberText));
    }

    public void validateShoppingFrequency(String shoppingFrequencyText) {
        textView.setError(InputValidator.isShopFrequencyValid(shoppingFrequencyText.equals("0") ? "" : shoppingFrequencyText));
    }
}
