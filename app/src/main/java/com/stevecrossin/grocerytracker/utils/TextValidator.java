package com.stevecrossin.grocerytracker.utils;

import android.widget.TextView;

public class TextValidator {
    private TextView textView;

    public TextValidator(TextView textView) {
        this.textView = textView;
    }

    public void validateName(String text) {
        textView.setError(InputValidator.isNameValid(text));
    }

    public void validateAge(String text) {
        textView.setError(InputValidator.isAgeValid(text));
    }

    public void validateHeight(String text) {
        textView.setError(InputValidator.isHeightValid(text));
    }

    public void validateWeight(String text) {
        textView.setError(InputValidator.isWeightValid(text));
    }

    public void validateEmail(String text) {
        textView.setError(InputValidator.isEmailValid(text));
    }

    public void validatePassword(String text) {
        textView.setError(InputValidator.isPasswordValid(text));
    }

    public void validateGender(String text) {
        textView.setError(InputValidator.isGenderValid(text.equals("0") ? "" : text));
    }

    public void validatePostcode(String text) {
        textView.setError(InputValidator.isPostcodeValid(text));
    }

    public void validateHouseholdNumber(String text) {
        textView.setError(InputValidator.isFamilyNumberValid(text));
    }

    public void validateAdultNumber(String text) {
        textView.setError(InputValidator.isAdultNumberValid(text));
    }

    public void validateChildNumber(String text) {
        textView.setError(InputValidator.isChildNumberValid(text));
    }

    public void validateSumOfAdultAndChildrenNumber(String total, String adult, String children) {
        textView.setError(InputValidator.isTotalFamilyNumberValid(total, adult, children));
    }

    public void validateShoppingFrequency(String text) {
        textView.setError(InputValidator.isShopFrequencyValid(text.equals("0") ? "" : text));
    }
}


//public abstract class TextValidator implements TextWatcher {
//
//    private TextView textView;
//
//    public TextValidator(TextView textView)
//    {
//        this.textView = textView;
//    }
//
//    public abstract void Validate(TextView textView, String field);
//
//
//    @Override
//    final public void afterTextChanged(Editable s){
//        String text= textView.getText().toString();
//        Validate(textView,text);
//    }
//
//
//    @Override
//    final public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
//
//    @Override
//    final public void onTextChanged(CharSequence s, int start, int before, int count) { }
//}

//        etName.addTextChangedListener(new TextValidator(etName) {
//@Override public void Validate(TextView textView, String text) {
//        if (!validator.isNameValid(text))
//        etName.setError("abc");
//        }
//        });