package com.stevecrossin.grocerytracker.screens;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.stevecrossin.grocerytracker.other.AppLoginState;
import com.stevecrossin.grocerytracker.other.PasswordScrambler;
import com.stevecrossin.grocerytracker.database.AppDataRepo;
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
    private View loginUiView;
    private Button loginButton;
    private AppLoginState appLoginState = INVALID_PASS;
    AppDataRepo repo = new AppDataRepo(Login.this);




/**
 * Check if user already logged in, and hasn't logged out. Will perform DB query defined lower, to check DB for users that match the loggedIn = true, and if so, it will skip login/sign up and navigate directly to main home page.
 *
 */

@Override
protected void onStart()
{
    super.onStart();
    //isSignedIn();

}

/***
 * Block here - need to do checks to see if email/password entered is valid. Code belongs here before the onCreate method is initialised rather than lower in the file, otherwise checks don't operate correctly.
 *
 */


/** Unwritten method, commented out. Will do the user signed in check
    private void isSignedIn() {
    }
*/

/**
 * OnCreate method to go here, that sets current view as login activity XML file, sets elements to display based on their ID, sets a listener for the views, and will add a textwatcher to username field to ensure details are valid.
 */
}