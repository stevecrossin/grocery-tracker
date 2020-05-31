package com.globe.grocerytracker.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.globe.grocerytracker.R;
import com.globe.grocerytracker.database.AppDataRepo;
import com.globe.grocerytracker.entities.User;


/**
 * Class which covers the splash screen of the application and its initialisation
 */
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_SplashTheme);
        super.onCreate(savedInstanceState);
        checkIfLoggedIn();
    }

    @SuppressLint("StaticFieldLeak")
    private void checkIfLoggedIn() {
        {
            new AsyncTask<Void, Void, User>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected User doInBackground(Void... voids) {
                    AppDataRepo repo = new AppDataRepo(SplashScreen.this);
                    return repo.getSignedUser();
                }

                @Override
                protected void onPostExecute(User user) {
                    super.onPostExecute(user);
                    if (user != null) {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreen.this, Login.class);
                        startActivity(intent);
                    }
                }
            }.execute();
        }
    }
}


