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
import android.widget.Toast;

import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.database.AppDataRepo;
import com.stevecrossin.grocerytracker.entities.User;
import com.stevecrossin.grocerytracker.other.PasswordScrambler;

public class Signup extends AppCompatActivity {

    Button Bsubmit, Bcancel;
    EditText etName, etAge, etHeight, etWeight,  etPostcode, etNumberOfHouseHoldMember, etHouseholdAdults, etHouseholdChildren, etEmail, etPassword;
    private AppDataRepo repository;
    private Login.LoginTask authenticationTask = null;
    private Spinner cbGender;

    private int selectedGenderPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        repository = new AppDataRepo(this);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        cbGender = findViewById(R.id.cbGender);
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
     * and they will be navigated to the welcome screen.
     */
    @SuppressLint("StaticFieldLeak")
    public void submitSignUp(View view) {
    String genderValue="";
        if(selectedGenderPosition==0){
            Toast.makeText(Signup.this, "Please select gender", Toast.LENGTH_LONG).show();
            return;
        }
        else
            genderValue = getResources().getStringArray(R.array.gender)[selectedGenderPosition];

        final User newUser;
        try {
            newUser = new User(etName.getText().toString(), etEmail.getText().toString(),
                    PasswordScrambler.scramblePassword(etPassword.getText().toString()), etAge.getText().toString(), etHeight.getText().toString(), etWeight.getText().toString(),
                    genderValue, etPostcode.getText().toString(),
                       etNumberOfHouseHoldMember.getText().toString(), etHouseholdAdults.getText().toString(), etHouseholdChildren.getText().toString());
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



