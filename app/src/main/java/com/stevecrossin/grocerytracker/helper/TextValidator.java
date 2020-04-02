package com.stevecrossin.grocerytracker.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

public class TextValidator{
    private TextView textView;
    protected InputValidator validator;

    public TextValidator(TextView textView)
    {
        this.textView = textView;
    }

    public void validateName(String text)
    {
        textView.setError(validator.isNameValid(text));
    }

    public void validateAge(String text)
    {
        textView.setError(validator.isAgeValid(text));
    }

    public void validateHeight(String text)
    {
        textView.setError(validator.isHeightValid(text));
    }

    public void validateWeight(String text)
    {
        textView.setError(validator.isWeightValid(text));
    }

    public void validateEmail(String text)
    {
        textView.setError(validator.isEmailValid(text));
    }

    public void validatePassword(String text)
    {
        textView.setError(validator.isPasswordValid(text));
    }

    public void validatePostcode(String text)
    {
        textView.setError(validator.isPostcodeValid(text));
    }

    public void validateHouseholdNumber(String text)
    {
        textView.setError(validator.isFamilyNumberValid(text));
    }

    public void validateAdultNumber(String text)
    {
        textView.setError(validator.isAdultNumberValid(text));
    }

    public void validateChildNumber(String text)
    {
        textView.setError(validator.isChildNumberValid(text));
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