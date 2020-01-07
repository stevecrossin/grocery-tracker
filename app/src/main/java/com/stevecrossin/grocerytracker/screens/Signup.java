package com.stevecrossin.grocerytracker.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.database.AppDataRepo;
import com.stevecrossin.grocerytracker.database.AppDb;
import com.stevecrossin.grocerytracker.entities.User;


public class Signup extends AppCompatActivity {

    Button Bsubmit, Bcancel;
    EditText etName, etAge, etHeight, etWeight, etGender, etPostcode, etNumberOfHouseHoldMember, etHouseHoldMkeup, etEmail, etPassword;
    private AppDataRepo repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        repository = new AppDataRepo(this);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etGender = findViewById(R.id.etGender);
        etPostcode = findViewById(R.id.etPostcode);
        etNumberOfHouseHoldMember = findViewById(R.id.etNumberOfHouseHoldMember);
        etHouseHoldMkeup = findViewById(R.id.etHouseHoldMkeup);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        Bsubmit = findViewById(R.id.Bsubmit);
        Bcancel = findViewById(R.id.Bcancel);

    }

    /*
    RG - Made some changes to add the data into a new User object, and then place into AppDB.
    NOTE: Not sure if it is storing properly.
     */

    @SuppressLint("StaticFieldLeak")
    public void submitSignUp(View view) {
        final User newUser = new User(etName.getText().toString(), etAge.getText().toString(), etHeight.getText().toString(), etWeight.getText().toString(), etGender.getText().toString(), etPostcode.getText().toString(),
                etNumberOfHouseHoldMember.getText().toString(), etHouseHoldMkeup.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString());
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                repository.insertUser(newUser);
                //Toasts don't work at this stage, removed.
                //Toast.makeText(getApplicationContext(),"User Added Successfully!", Toast.LENGTH_LONG).show();
                return null;
            }
        }.execute();

        Intent intent = new Intent (this, Welcome.class);
        startActivity(intent);
    }
    public void cancelSignUp(View view) {
        Intent intent = new Intent (this, Login.class);
        startActivity(intent);
    }
   }



