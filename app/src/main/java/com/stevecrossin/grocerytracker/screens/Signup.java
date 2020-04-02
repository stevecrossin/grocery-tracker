package com.stevecrossin.grocerytracker.screens;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.database.AppDataRepo;
import com.stevecrossin.grocerytracker.entities.User;
import com.stevecrossin.grocerytracker.helper.InputValidator;
import com.stevecrossin.grocerytracker.helper.TextValidator;
import com.stevecrossin.grocerytracker.other.PasswordScrambler;

import java.util.ArrayList;

import javax.xml.validation.Validator;

public class Signup extends AppCompatActivity {
    Button Bsubmit, Bcancel;
    EditText etName, etAge, etHeight, etWeight,  etPostcode, etNumberOfHouseHoldMember, etHouseholdAdults, etHouseholdChildren, etEmail, etPassword;
    private AppDataRepo repository;
    private Login.LoginTask authenticationTask = null;
    private Spinner cbGender;
    private Spinner cbShopNumber;
    private InputValidator validator;
    private TextValidator textValidator;
    private int selectedGenderPosition=0;
    private int selectedShopNumber=0;
    ArrayAdapter<CharSequence> adapter;


    private void InitializeView()
    {
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        cbGender = findViewById(R.id.cbGender);
        cbShopNumber = findViewById(R.id.cbShopNumber);
        etPostcode = findViewById(R.id.etPostcode);
        etNumberOfHouseHoldMember = findViewById(R.id.etNumberOfHouseHoldMember);
        etHouseholdAdults = findViewById(R.id.etHouseHoldAdults);
        etHouseholdChildren = findViewById(R.id.etHouseHoldChild);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        Bsubmit = findViewById(R.id.Bsubmit);
        Bcancel = findViewById(R.id.Bcancel);

        adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_dropdown_item);

        cbGender.setAdapter(adapter);
        cbGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGenderPosition = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.shopnumber, android.R.layout.simple_spinner_dropdown_item);

        cbShopNumber.setAdapter(adapter2);
        cbShopNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedShopNumber = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void ValidateName()
    {
        textValidator = new TextValidator(etName);
        textValidator.validateName(etName.getText().toString());
    };

    private void ValidateAge()
    {
        textValidator = new TextValidator(etAge);
        textValidator.validateAge(etAge.getText().toString());
    }

    private void ValidateHeight()
    {
        textValidator = new TextValidator(etHeight);
        textValidator.validateHeight(etHeight.getText().toString());
    }

    private void ValidateWeight()
    {
        textValidator = new TextValidator(etWeight);
        textValidator.validateWeight(etWeight.getText().toString());
    }

    private void ValidateEmail()
    {
        textValidator = new TextValidator(etEmail);
        textValidator.validateEmail(etEmail.getText().toString());
    }

    private void ValidatePassword()
    {
        textValidator = new TextValidator(etPassword);
        textValidator.validatePassword(etPassword.getText().toString());
    }

    private void ValidatePostcode()
    {
        textValidator = new TextValidator(etPostcode);
        textValidator.validatePostcode(etPostcode.getText().toString());
    }

    private void ValidateHouseholdNumber()
    {
        textValidator = new TextValidator(etNumberOfHouseHoldMember);
        textValidator.validateHouseholdNumber(etNumberOfHouseHoldMember.getText().toString());
    }

    private void ValidateAdultNumber()
    {
        textValidator = new TextValidator(etHouseholdAdults);
        textValidator.validateAdultNumber(etHouseholdAdults.getText().toString());
    }

    private void ValidateChildNumber()
    {
        textValidator = new TextValidator(etHouseholdChildren);
        textValidator.validateChildNumber(etHouseholdChildren.getText().toString());
    }


    // Missing Gender and shopping Frequency



    private void ValidateOnTheFly()
    {
        // Missing Gender and shopping Frequency

        etName.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateName();
            }
        });

        etAge.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateAge();
            }
        });

        etHeight.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateHeight();
            }
        });

        etWeight.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateWeight();
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateEmail();
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidatePassword();
            }
        });

        //Gender


        etPostcode.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidatePostcode();
            }
        });

        etNumberOfHouseHoldMember.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateHouseholdNumber();
            }
        });

        etHouseholdAdults.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateAdultNumber();
            }
        });

        etHouseholdChildren.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateChildNumber();
            }
        });

        //Frequency


    }

    private boolean isFormValid()
    {
        ValidateName();
        ValidateAge();
        ValidateEmail();
        ValidatePassword();
        ValidateHeight();
        ValidateWeight();
        ValidatePostcode();
        ValidateHouseholdNumber();
        ValidateAdultNumber();
        ValidateChildNumber();



        // Missing Gender and shopping Frequency
        return (etName.getError()==null) && (etAge.getError()==null) && (etHeight.getError()==null)
                && (etWeight.getError()==null) && (etEmail.getError()==null) && (etPassword.getError()==null)
                && (etPostcode.getError()==null)
                && (etNumberOfHouseHoldMember.getError()==null) && (etHouseholdAdults.getError()==null) && (etHouseholdChildren.getError()==null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        repository = new AppDataRepo(this);
        InitializeView();
        ValidateOnTheFly();
    }

    /*
    RG - Made some changes to add the data into a new User object, and then place into AppDB.
    NOTE: Not sure if it is storing properly.
     */
    /**
     * Submits sign up. Checks if gender was selected, if not, shows toast and returns to the task
     * After this validation check passes. it will run the insertUser method from AppDataRepo. This method has an exception check -
     * as the email needs to be a unique field, if the end user tries to sign up with an account that already exists, the operation will fail,
     * a Toast will be shown advising email already exists and to login, and then will navigate them to that activity. Otherwise, the insert will be successful
     * and they will be navigated to the welcome screen */
    @SuppressLint("StaticFieldLeak")
    public void submitSignUp(View view) {
        String genderValue = "";
        boolean cancel = false;
        View focusView = null;
        if (selectedGenderPosition == 0) {
            Toast.makeText(Signup.this, "Please select gender", Toast.LENGTH_LONG).show();
            return;
        } else
            genderValue = getResources().getStringArray(R.array.gender)[selectedGenderPosition];

        String shopNumberValue = "";
        if (selectedShopNumber == 0) {

            Toast.makeText(Signup.this, "Please select how often you shop", Toast.LENGTH_LONG).show();
            return;
        } else
            shopNumberValue = getResources().getStringArray(R.array.shopnumber)[selectedShopNumber];

        if (!isFormValid())
        {
            Toast.makeText(Signup.this, "Form is invalid", Toast.LENGTH_LONG).show();
            return;
        }

        String sTotalHouseholdMember = etNumberOfHouseHoldMember.getText().toString();
        int totalHouseholdMember = Integer.parseInt(sTotalHouseholdMember);

        String sHouseholdAdults = etHouseholdAdults.getText().toString();
        int householdAdults = Integer.parseInt(sHouseholdAdults);

        String sHouseholdChildren = etHouseholdChildren.getText().toString();
        int householdChildren = Integer.parseInt(sHouseholdChildren);

        if (totalHouseholdMember != (householdAdults + householdChildren)) {
            etNumberOfHouseHoldMember.setError("Household members don't match");
            focusView = etNumberOfHouseHoldMember;
            focusView.requestFocus();
            return;

            }
//
//
//
//        String sAge = etAge.getText().toString();
//        int age = Integer.parseInt(sAge);
//
//        if (age < 16 || age > 100 )
//        {
//            etAge.setError("You are not eligible to sign up");
//            focusView = etAge;
//            focusView.requestFocus();
//            //Toast.makeText(Signup.this, "You are not eligible to sign up", Toast.LENGTH_LONG).show();
//            return;
//        }
//        //else if (age > 100)
//        //{
//         //   Toast.makeText(Signup.this, "Please enter a valid age", Toast.LENGTH_LONG).show();
//           // return;
//        //}


        final User newUser;
        try {
            newUser = new User(etName.getText().toString(), etEmail.getText().toString(),
                    PasswordScrambler.scramblePassword(etPassword.getText().toString()), etAge.getText().toString(), etHeight.getText().toString(), etWeight.getText().toString(),
                    genderValue, etPostcode.getText().toString(),
                    etNumberOfHouseHoldMember.getText().toString(), etHouseholdAdults.getText().toString(), etHouseholdChildren.getText().toString(), shopNumberValue);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    if(repository.insertUser(newUser))
                    {
                        Intent intent = new Intent (Signup.this, Welcome.class);
                        startActivity(intent);
                    }
                    else
                    {
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Signup.this, "An account with this email address already exists, please login", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent (Signup.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }
                    return null;
                }
            }.execute();
        }  catch (Exception ex) {
            Toast.makeText(Signup.this, "Error scrambling password", Toast.LENGTH_SHORT).show();
        }
    }
    public void cancelSignUp(View view) {
        Intent intent = new Intent (this, Login.class);
        startActivity(intent);
    }


}
