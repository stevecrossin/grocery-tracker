package com.stevecrossin.grocerytracker.screens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.database.AppDataRepo;
import com.stevecrossin.grocerytracker.entities.User;
import com.stevecrossin.grocerytracker.utils.AppLoginState;
import com.stevecrossin.grocerytracker.utils.PasswordScrambler;
import com.stevecrossin.grocerytracker.utils.TextValidator;

import java.util.List;

import static com.stevecrossin.grocerytracker.utils.AppLoginState.EXISTING_ACCOUNT;
import static com.stevecrossin.grocerytracker.utils.AppLoginState.HASH_ERROR;
import static com.stevecrossin.grocerytracker.utils.AppLoginState.INVALID_PASS;
import static com.stevecrossin.grocerytracker.utils.AppLoginState.INVALID_USER;

public class Login extends AppCompatActivity {

    /**
     * Initialisation of objects/variables. Helps to setup the view, keep track of user login state
     */
    private LoginTask authTask = null;
    private View loginProg;
    private View loginUIView;
    private AutoCompleteTextView usernameView;
    private EditText passwordView;
    private Button loginButton;
    private AppLoginState appLoginState = INVALID_PASS;
    private StorageReference mStorageRef;
    private TextValidator textValidator;
    private TextInputLayout loginPasswordDetail;

    /**
     * Check if user already logged in, and hasn't logged out. Will perform DB query defined lower, to check DB for users that match the loggedIn = true, and if so, it will skip login/sign up and navigate directly to main home page.
     */

    @Override
    protected void onStart() {
        super.onStart();
        isSignedIn();
    }

    /**
     * OnCreate method to go here, that sets current view as login activity XML file, sets elements to display based on their ID, sets a listener for the views, and will add a textwatcher to username field to ensure details are valid.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        loginUIView = findViewById(R.id.signInForm);
        loginProg = findViewById(R.id.loginprog);
        usernameView = findViewById(R.id.enterUsername);
        passwordView = findViewById(R.id.enterPassword);
        loginPasswordDetail = findViewById(R.id.loginPasswordDetail);
        loginButton = findViewById(R.id.loginButton);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    tryLogin();
                    return true;
                }
                return false;
            }
        });

//        passwordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    loginPasswordDetail.setEndIconVisible(true);
//                }
//            }
//        });
    }

    @SuppressLint("StaticFieldLeak")
    private void isSignedIn() {
        new AsyncTask<Void, Void, User>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loginProg.setVisibility(View.VISIBLE);
            }

            @Override
            protected User doInBackground(Void... voids) {
                AppDataRepo repo = new AppDataRepo(Login.this);
                return repo.getSignedUser();
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                loginProg.setVisibility(View.GONE);
                if (user != null) {
                    goToNextScreen();
                } else {
                    loginButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tryLogin();
                        }
                    });
                }
            }
        }.execute();
    }

    /**
     * After login is successful, this will finish the activity, and start the MainActivity class, which will then perform its respective operations.
     */
    private void goToNextScreen() {
        finish();
        startActivity(new Intent(Login.this, MainActivity.class));
    }

    private void ValidateEmail() {
        textValidator = new TextValidator(usernameView);
        textValidator.validateEmail(usernameView.getText().toString());
    }

    private void ValidatePassword() {
        textValidator = new TextValidator(passwordView);
        textValidator.validatePassword(passwordView.getText().toString());

    }

    private boolean isFormValid() {
        ValidateEmail();
        ValidatePassword();
        return (usernameView.getError() == null) && (passwordView.getError() == null);
    }

    /**
     * This handles the attempt to login, after the login/sign in button is clicked. It will only call the async task if it's not running.
     * <p>
     * By default, no UI errors are displayed and once the UI error has been displayed, if any, when the user interface is clicked, any errors will be hidden/removed
     * <p>
     * Once the user has entered all their input, and the login button has been clicked, it will convert the text in the username and password fields to a string.
     * It will then determine perform password and username validation and depending on the rules, e.g. if it was empty or the format doesn't match the rules, errors will be thrown and
     * the request to login will be cancelled and an error message will be displayed.
     ***/
    private void tryLogin() {
        if (authTask != null) {
            return;
        }

        if (!isFormValid())
        {
            if (usernameView.getError() != null)
                usernameView.requestFocus();
            else if (passwordView.getError() != null) ;
                passwordView.requestFocus();
        }
        else {
            showProgressUI(true);
            String username = usernameView.getText().toString();
            String password = passwordView.getText().toString();
            authTask = new LoginTask(username, password);
            authTask.execute((Void) null);
        }

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgressUI(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        loginUIView.setVisibility(show ? View.GONE : View.VISIBLE);
        loginUIView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginUIView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        loginProg.setVisibility(show ? View.VISIBLE : View.GONE);
        loginProg.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    @SuppressLint("StaticFieldLeak")
    public class LoginTask extends AsyncTask<Void, Void, AppLoginState> {

        private final String email;
        private final String passKey;

        LoginTask(String email, String password) {
            this.email = email;
            passKey = password;
        }

        /**
         * Handles login or signup of the user in the background. If email is existing, sign in will be attempted, which will compare the string in the password entered,
         * and convert it to a hash using PasswordScrambler class, and then compare with the value that is stored in the database. If it is a match, login will be allowed and login state will change to
         * EXISTING_ACCOUNT.
         * <p>
         * However, if there is no match, the default status is INVALID_PASS which will deny login. If for some reason the application fails to get a password hash (this will never happen, but in case), an exception is logged
         * and a toast will be displayed, as noted in onPostExecute.
         */
        @Override
        protected AppLoginState doInBackground(Void... params) {
            try {
                String hashPass = PasswordScrambler.scramblePassword(passKey);
                User user = new AppDataRepo(Login.this).getUserByEmail(email);
                if (user == null) {
                    return INVALID_USER;
                }
                if (!user.getPassKey().equals(hashPass)) {
                    return INVALID_PASS;
                }

                onLoginSuccess(user.getUserID());
                return EXISTING_ACCOUNT;
            } catch (Exception e) {
                return HASH_ERROR;
            }
        }

        /**
         * Actions will take place in the UI based on the result of this background thread AppLoginState.
         * If the authentication against existing user was successful, the activity will end and MainActivity will load, otherwise an error will be thrown that the password is incorrect.
         */
        @Override
        protected void onPostExecute(final AppLoginState success) {
            authTask = null;
            showProgressUI(false);
            if (success == EXISTING_ACCOUNT) {
                goToNextScreen();
            } else if (success == HASH_ERROR) {
                Toast.makeText(Login.this, "Error getting password hash", Toast.LENGTH_SHORT).show();
            }
            else if (success == INVALID_USER) {
                usernameView.setError("User does not exist");
                usernameView.requestFocus();
                loginButton.setText(getString(R.string.sign_in_text));
            }
            else {
                passwordView.setError("Password Incorrect",null);
//                loginPasswordDetail.setEndIconVisible(false);
                passwordView.requestFocus();
                loginButton.setText(getString(R.string.sign_in_text));
            }
        }

        /**
         * Code to execute if the background tasks are terminated. Will hide the progressUI.
         */
        @Override
        protected void onCancelled() {
            authTask = null;
            showProgressUI(false);
        }
    }

    /**
     * On successful login, this method will take the user ID, and create a new instance of AppDataRepo.
     */
    private void onLoginSuccess(Integer userId) {
        AppDataRepo repo = new AppDataRepo(Login.this);
        repo.updateLoginStatus(userId, true);
    }

    /**
     * This is an OnClick method that is called when the "Sign Up" button is clicked in the activity. It will load the Sign Up class.
     */
    public void navSignUp(View view) {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }
}