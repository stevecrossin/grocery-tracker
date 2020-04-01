package com.stevecrossin.grocerytracker.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

public abstract class TextValidator implements TextWatcher {

    private TextView textView;

    public TextValidator(TextView textView)
    {
        this.textView = textView;
    }

    public abstract void Validate(TextView textView, String field);


    @Override
    final public void afterTextChanged(Editable s){
        String text= textView.getText().toString();
        Validate(textView,text);
    }


    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { }
}

//        etName.addTextChangedListener(new TextValidator(etName) {
//@Override public void Validate(TextView textView, String text) {
//        if (!validator.isNameValid(text))
//        etName.setError("abc");
//        }
//        });