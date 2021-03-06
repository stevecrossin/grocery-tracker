package com.globe.grocerytracker.screens;

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

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.globe.grocerytracker.R;
import com.globe.grocerytracker.database.AppDataRepo;
import com.globe.grocerytracker.entities.User;
import com.globe.grocerytracker.models.UserReceipt;
import com.globe.grocerytracker.utils.TextValidator;
import com.globe.grocerytracker.utils.PasswordScrambler;

public class Signup extends AppCompatActivity {

    /**
     * Initialisation of objects/variables. Helps to setup the view
     */
    private AppDataRepo repository;
    private Login.LoginTask authenticationTask = null;
    DatabaseReference databaseReference;

    private TextValidator textValidator;

    Button Bsubmit, Bcancel;
    EditText etName, etAge, etHeight, etWeight, etPostcode, etNumberOfHouseHoldMember, etHouseholdAdults, etHouseholdChildren, etEmail, etPassword;
    private Spinner cbGender;
    private Spinner cbShopNumber;
    public TextInputLayout passwordLayoutDetail;
    ArrayAdapter<CharSequence> adapter;

    private int selectedShopNumber = 0;
    private int selectedGenderPosition = 0;

    /**
     * Initialize the views, add in listener and add the adapter for gender and shop frequency spinners
     */
    private void InitializeView() {
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
        passwordLayoutDetail = findViewById(R.id.passwordLayoutDetail);

        /*
         * Enables the gender and shop number boxes to be focusable and request focus
         */
        cbGender.setFocusable(true);
        cbGender.setFocusableInTouchMode(true);
        cbGender.requestFocus();

        cbShopNumber.setFocusable(true);
        cbShopNumber.setFocusableInTouchMode(true);
        cbShopNumber.requestFocus();

        cbGender.setAdapter(adapter);
        cbGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGenderPosition = position;
                if (position > 0) {
                    etPostcode.requestFocus();
                }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        repository = new AppDataRepo(this);
        InitializeView();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        ValidateOnTheFly();

    }

    /**
     * Submits sign up. Checks if gender was selected, if not, shows toast and returns to the task
     * After this validation check passes. it will run the insertUser method from AppDataRepo. This method has an exception check -
     * as the email needs to be a unique field, if the end user tries to sign up with an account that already exists, the operation will fail,
     * a Toast will be shown advising email already exists and to login, and then will navigate them to that activity. Otherwise, the insert will be successful
     * and they will be navigated to the welcome screen
     */
    @SuppressLint("StaticFieldLeak")
    public void submitSignUp(View view) {
        final AppDataRepo dataRepo = new AppDataRepo(this);
        if (!isFormValid()) {
            Toast.makeText(Signup.this, "Form is invalid", Toast.LENGTH_LONG).show();
            return;
        }
        final User newUser;
        try {
            newUser = new User(etName.getText().toString(), etEmail.getText().toString(),
                    PasswordScrambler.scramblePassword(etPassword.getText().toString()), etAge.getText().toString(),
                    etHeight.getText().toString(), etWeight.getText().toString(),
                    getResources().getStringArray(R.array.gender)[selectedGenderPosition], etPostcode.getText().toString(),
                    etNumberOfHouseHoldMember.getText().toString(), etHouseholdAdults.getText().toString(), etHouseholdChildren.getText().toString(),
                    getResources().getStringArray(R.array.shopnumber)[selectedShopNumber]);
            newUser.setLoggedIn(true);
            new AsyncTask<Void, Void, Void>() {
                @Override
                /**
                 * Creates new instance of userReceipt class, gets current user signed in, pushes current user info in DB to firebase, moves to welcome screen via intent.
                 */
                protected Void doInBackground(Void... voids) {
                    if (repository.insertUser(newUser)) {
                        UserReceipt userReceipt = new UserReceipt();
                        User currentUser = dataRepo.getSignedUser();
                        userReceipt.user = new User(currentUser);
                        String id = databaseReference.push().getKey();
                        databaseReference.child(id).setValue(userReceipt);
                        Intent intent = new Intent(Signup.this, Welcome.class);
                        finish();
                        startActivity(intent);
                    } else {
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Signup.this, "An account with this email address already exists, please login", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Signup.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }
                    return null;
                }
            }.execute();
        } catch (Exception ex) {
            Toast.makeText(Signup.this, "Error scrambling password", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Direct to Login page when cancelling sign up
     */
    public void cancelSignUp(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    /**
     * Trigger the validation whenever the user move out of the field
     */
    private void ValidateOnTheFly() {
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateName();
            }
        });

        etAge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateAge();
            }
        });

        etHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateHeight();
            }
        });

        etWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateWeight();
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateEmail();
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ValidatePassword();
                    if (etPassword.getError() != null) {
                        String error = etPassword.getError().toString();
                        passwordLayoutDetail.setEndIconVisible(false);
                        etPassword.setError(error);
                    }
                } else if (hasFocus) {
                    passwordLayoutDetail.setEndIconVisible(true);
                    if (etPassword.getError() != null) {
                        String error = etPassword.getError().toString();
                        etPassword.setError(error, null);
                    }
                }
            }
        });

        cbGender.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateGender();
            }
        });

        etPostcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidatePostcode();
            }
        });

        etNumberOfHouseHoldMember.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateHouseholdNumber();
            }
        });

        etHouseholdAdults.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateAdultNumber();
            }
        });

        etHouseholdChildren.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateChildNumber();
            }
        });

        cbShopNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) ValidateShoppingFrequency();
            }
        });

    }

    /**
     * Validate the form to ensure the validity of all fields before doing the sign up for the user
     * @return boolean variable of the validity of the current state of form
     */
    private boolean isFormValid() {
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
        ValidateGender();
        ValidateShoppingFrequency();

        return (etName.getError() == null) && (etAge.getError() == null) && (etHeight.getError() == null)
                && (etWeight.getError() == null) && (etEmail.getError() == null) && (etPassword.getError() == null)
                && (etPostcode.getError() == null)
                && (cbGender.getSelectedItemPosition() != 0) && (cbShopNumber.getSelectedItemPosition() != 0)
                && (etNumberOfHouseHoldMember.getError() == null) && (etHouseholdAdults.getError() == null) && (etHouseholdChildren.getError() == null);
    }


    private void ValidateName() {
        textValidator = new TextValidator(etName);
        textValidator.validateName(etName.getText().toString());
    }

    private void ValidateAge() {
        textValidator = new TextValidator(etAge);
        textValidator.validateAge(etAge.getText().toString());
    }

    private void ValidateHeight() {
        textValidator = new TextValidator(etHeight);
        textValidator.validateHeight(etHeight.getText().toString());
    }

    private void ValidateWeight() {
        textValidator = new TextValidator(etWeight);
        textValidator.validateWeight(etWeight.getText().toString());
    }

    private void ValidateGender() {
        textValidator = new TextValidator((TextView) cbGender.getSelectedView());
        textValidator.validateGender(Integer.toString(selectedGenderPosition));
    }

    private void ValidateEmail() {
        textValidator = new TextValidator(etEmail);
        textValidator.validateEmail(etEmail.getText().toString());
    }

    private void ValidatePassword() {
        textValidator = new TextValidator(etPassword);
        textValidator.validatePassword(etPassword.getText().toString());

    }

    private void ValidatePostcode() {
        textValidator = new TextValidator(etPostcode);
        textValidator.validatePostcode(etPostcode.getText().toString());
    }

    private void ValidateHouseholdNumber() {
        textValidator = new TextValidator(etNumberOfHouseHoldMember);
        textValidator.validateHouseholdNumber(etNumberOfHouseHoldMember.getText().toString(), etHouseholdAdults.getText().toString(), etHouseholdChildren.getText().toString());
    }

    private void ValidateAdultNumber() {
        textValidator = new TextValidator(etHouseholdAdults);
        textValidator.validateAdultNumber(etHouseholdAdults.getText().toString());
        ValidateHouseholdNumber();
    }


    private void ValidateChildNumber() {
        textValidator = new TextValidator(etHouseholdChildren);
        textValidator.validateChildNumber(etHouseholdChildren.getText().toString());
        ValidateHouseholdNumber();

    }

    private void ValidateShoppingFrequency() {
        textValidator = new TextValidator((TextView) cbShopNumber.getSelectedView());
        textValidator.validateShoppingFrequency(Integer.toString(selectedShopNumber));
    }


}