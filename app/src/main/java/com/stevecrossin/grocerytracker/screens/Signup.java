package com.stevecrossin.grocerytracker.screens;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

import javax.xml.validation.Validator;

public class Signup extends AppCompatActivity {
    Button Bsubmit, Bcancel;
    EditText etName, etAge, etHeight, etWeight,  etPostcode, etNumberOfHouseHoldMember, etHouseholdAdults, etHouseholdChildren, etEmail, etPassword;
    private AppDataRepo repository;
    private Login.LoginTask authenticationTask = null;
    private Spinner cbGender;
    private Spinner cbShopNumber;
    private InputValidator validator;
    private int selectedGenderPosition=0;
    private int selectedShopNumber=0;


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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_dropdown_item);

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

    private void ValidateOnTheFly()
    {
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = etName.getText().toString();
                    String error = validator.isNameValid(text);
                    if (error!=null)
                        etName.setError(error);

                }
            }
        });

        etAge.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = etAge.getText().toString();
                    String error = validator.isAgeValid(text);
                    if (error!=null)
                        etAge.setError(error);
                }
            }
        });

        etHeight.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = etHeight.getText().toString();
                    String error = validator.isHeightValid(text);
                    if (error!=null)
                        etHeight.setError(error);
                }
            }
        });

        etWeight.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = etWeight.getText().toString();
                    String error = validator.isWeightValid(text);
                    if (error!=null)
                        etWeight.setError(error);
                }
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = etEmail.getText().toString();
                    String error = validator.isEmailValid(text);
                    if (error!=null)
                        etEmail.setError(error);
                }
            }
        });


        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = etPassword.getText().toString();
                    String error = validator.isPasswordValid(text);
                    if (error!=null)
                        etPassword.setError(error);

                }
            }
        });

        //Gender
//        etAge.setOnFocusChangeListener(new View.OnFocusChangeListener(){
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    String text = etAge.getText().toString();
//                    String error = validator.isAgeValid(text);
//                    if (error!=null)
//                        etAge.setError(error);
//                }
//            }
//        });

        etPostcode.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = etPostcode.getText().toString();
                    String error = validator.isPostcodeValid(text);
                    if (error!=null)
                        etPostcode.setError(error);
                }
            }
        });

        etNumberOfHouseHoldMember.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = etNumberOfHouseHoldMember.getText().toString();
                    String error = validator.isFamilyNumberValid(text);
                    if (error!=null)
                        etNumberOfHouseHoldMember.setError(error);
                }
            }
        });

        etHouseholdAdults.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = etHouseholdAdults.getText().toString();
                    String error = validator.isAdultNumberValid(text);
                    if (error!=null)
                        etHouseholdAdults.setError(error);
                }
            }
        });

        etHouseholdChildren.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = etHouseholdChildren.getText().toString();
                    String error = validator.isChildNumberValid(text);
                    if (error!=null)
                        etHouseholdChildren.setError(error);
                }
            }
        });

        //Frequency
//        etHouseholdAdults.setOnFocusChangeListener(new View.OnFocusChangeListener(){
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    String text = etHouseholdAdults.getText().toString();
//                    String error = validator.isAdultNumberValid(text);
//                    if (error!=null)
//                        etHouseholdAdults.setError(error);
//                }
//            }
//        });


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


//        String sTotalHouseholdMember = etNumberOfHouseHoldMember.getText().toString();
//        int totalHouseholdMember = Integer.parseInt(sTotalHouseholdMember);
//
//        String sHouseholdAdults = etHouseholdAdults.getText().toString();
//        int householdAdults = Integer.parseInt(sHouseholdAdults);
//
//        String sHouseholdChildren = etHouseholdChildren.getText().toString();
//        int householdChildren = Integer.parseInt(sHouseholdChildren);
//
//        if (totalHouseholdMember != (householdAdults + householdChildren)) {
//            etNumberOfHouseHoldMember.setError("Household members don't match");
//            focusView = etNumberOfHouseHoldMember;
//            focusView.requestFocus();
//            return;
//
//            }
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
