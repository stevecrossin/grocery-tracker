package com.stevecrossin.grocerytracker.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.stevecrossin.grocerytracker.R;


public class Signup extends AppCompatActivity implements View.OnClickListener {

    Button Bsubmit, Bcancel;
    EditText etName, etAge, etHeight, etWeight, etGender, etPostcode, etNumberOfHouseHoldMember, etHouseHoldMkeup, etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        etHeight = (EditText) findViewById(R.id.etHeight);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etGender = (EditText) findViewById(R.id.etGender);
        etPostcode = (EditText) findViewById(R.id.etPostcode);
        etNumberOfHouseHoldMember = (EditText) findViewById(R.id.etNumberOfHouseHoldMember);
        etHouseHoldMkeup = (EditText) findViewById(R.id.etHouseHoldMkeup);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        Bsubmit = (Button) findViewById(R.id.Bsubmit);
        Bcancel = (Button) findViewById(R.id.Bcancel);

        Bsubmit.setOnClickListener(this);
        Bcancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Bsubmit:
                /* once main menu is created (next page from here), then appropriate action goes here.*/
                break;
            case R.id.Bcancel:
                startActivity(new Intent(this, Login.class));
                break;
        }
    }
}

