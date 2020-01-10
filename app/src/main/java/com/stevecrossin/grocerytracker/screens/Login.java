package com.stevecrossin.grocerytracker.screens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.database.AppDataRepo;
import com.stevecrossin.grocerytracker.entities.User;
import com.stevecrossin.grocerytracker.other.AppLoginState;
import com.stevecrossin.grocerytracker.other.PasswordScrambler;

import java.util.List;

import static com.stevecrossin.grocerytracker.other.AppLoginState.EXISTING_ACCOUNT;
import static com.stevecrossin.grocerytracker.other.AppLoginState.HASH_ERROR;
import static com.stevecrossin.grocerytracker.other.AppLoginState.INVALID_PASS;
import static com.stevecrossin.grocerytracker.other.AppLoginState.INVALID_USER;

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

    /**
     * Check if user already logged in, and hasn't logged out. Will perform DB query defined lower, to check DB for users that match the loggedIn = true, and if so, it will skip login/sign up and navigate directly to main home page.
     */

    @Override
    protected void onStart() {
        super.onStart();
        isSignedIn();

    }
    public static boolean isEmaiValid(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
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

        usernameView.setError(null);
        passwordView.setError(null);

        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!isPasswordValid(password)) {
            passwordView.setError("Password is invalid");
            focusView = passwordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(username)) {
            usernameView.setError("Field cannot be empty");
            focusView = usernameView;
            cancel = true;
        } else if (!isEmaiValid(username)) {
            usernameView.setError("Email address entered is invalid");
            focusView = usernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgressUI(true);
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

        private final String userName;
        private final String passKey;

        LoginTask(String email, String password) {
            userName = email;
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
                    List<User> users = new AppDataRepo(Login.this).getAllUsers();

                    int i;
                    for (i = 0; i < users.size(); i++) {
                     if(users.get(i).getEmail().equalsIgnoreCase(userName)){
                         if (users.get(i).getPassKey().equals(hashPass)) {
                             onLoginSuccess(users.get(i).getUserID());
                             return EXISTING_ACCOUNT;
                         }else {
                             return INVALID_PASS;
                         }
                     }

                    }

                    if(i == users.size())
                        return INVALID_USER;
                }
                catch (Exception e) {
                    return HASH_ERROR;
                }
                return INVALID_PASS;
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
                passwordView.setError("Password Incorrect");
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