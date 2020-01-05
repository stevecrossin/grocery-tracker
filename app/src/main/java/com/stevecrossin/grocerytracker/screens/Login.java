package com.stevecrossin.grocerytracker.screens;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.other.AppLoginState;
import com.stevecrossin.grocerytracker.other.PasswordScrambler;
import com.stevecrossin.grocerytracker.database.AppDataRepo;
import com.stevecrossin.grocerytracker.entities.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.stevecrossin.grocerytracker.other.AppLoginState.NEW_ACCOUNT;
import static com.stevecrossin.grocerytracker.other.AppLoginState.EXISTING_ACCOUNT;
import static com.stevecrossin.grocerytracker.other.AppLoginState.INVALID_PASS;
import static com.stevecrossin.grocerytracker.other.AppLoginState.HASH_ERROR;

public class Login extends AppCompatActivity {
    /* Login code for the application will go here.

     Some of the functions that will need to be performed are:
     1. Initialise the UI elements on the screen, setup current login state
     2. Perform checks to see if a user has logged in
     3. Perform authentication, and navigate to main activity.
     */

    /**
     * Initialisation of objects/variables. Helps to setup the view, keep track of user login state
     */
    //private LoginOperation authTask = null;
    private AutoCompleteTextView enterUsername;
    private EditText enterPassword;
    private View loginProg;
    private View loginUIView;
    private AutoCompleteTextView usernameView;
    private EditText passwordView;
    private Button loginButton;
    private AppLoginState appLoginState = INVALID_PASS;
    //AppDataRepo repo = new AppDataRepo(Login.this); - buggy code, need to fix DB


    /**
     * Check if user already logged in, and hasn't logged out. Will perform DB query defined lower, to check DB for users that match the loggedIn = true, and if so, it will skip login/sign up and navigate directly to main home page.
     */

    @Override
    protected void onStart() {
        super.onStart();
        //isSignedIn();

    }

    /***
     * isValidEmail: checks to see if email is not empty and also is a valid email address.
     */
    private boolean isValidEmail() {
        String emailAddress = enterUsername.getText().toString();
        if (null == emailAddress || emailAddress.length() == 0) {
            return false;
        }
        Pattern emailPattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        return emailPattern.matcher(emailAddress).matches();
    }

    /***
     * isValidPassword: checks to see if password entered is valid.
     * It requires the password to at least 1 digit,
     * at least 1 capital letter, at least 1 special character (@#$%^&+=!),
     * no white space and be at least 4 characters long.
     */
    private boolean isValidPassword() {
        String passwordValue = enterPassword.getText().toString();
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.* [@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(passwordValue);
        return matcher.matches();
    }



/** Unwritten method, commented out. Will do the user signed in check
    private void isSignedIn() {
    }
*/

/**
 * OnCreate method to go here, that sets current view as login activity XML file, sets elements to display based on their ID, sets a listener for the views, and will add a textwatcher to username field to ensure details are valid.
 */
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    loginUIView = findViewById(R.id.signInForm);
    loginProg = findViewById(R.id.loginprog);
    usernameView = findViewById(R.id.enterUsername);
    passwordView = findViewById(R.id.passwordEntry);
    loginButton = findViewById(R.id.loginButton);
}

    /**
     * This is an OnClick method that is called when the "Pick Ingredients" button is clicked in the activity. It will load the CategoryPicker.class/
     */
    public void navSignUp(View view) {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }
}