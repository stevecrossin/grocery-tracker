package com.stevecrossin.grocerytracker.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.database.AppDb;
import com.stevecrossin.grocerytracker.entities.User;


public class Signup extends AppCompatActivity implements View.OnClickListener {

    Button Bsubmit, Bcancel;
    EditText etName, etAge, etHeight, etWeight, etGender, etPostcode, etNumberOfHouseHoldMember, etHouseHoldMkeup, etEmail, etPassword;
    public static AppDb userAppDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        etHeight = (EditText) findViewById(R.id.etHeight);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etGender = (EditText) findViewById(R.id.etGender);
        etPostcode = (EditText) findViewById(R.id.etPostcode);
        etNumberOfHouseHoldMember = (EditText) findViewById(R.id.etNumberOfHouseHoldMember);
        etHouseHoldMkeup = (EditText) findViewById(R.id.etHouseHoldMkeup);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        Bsubmit = (Button) findViewById(R.id.Bsubmit);
        Bcancel = (Button) findViewById(R.id.Bcancel);

        Bsubmit.setOnClickListener(this);
        Bcancel.setOnClickListener(this);

        userAppDb = Room.databaseBuilder(getApplicationContext(), AppDb.class, "userdb").build();

    }

    /*
    RG - Made some changes to add the data into a new User object, and then place into AppDB.
    NOTE: Not sure if it is storing properly.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Bsubmit:
                /* once main menu is created (next page from here), then appropriate action goes here.*/

                User user = new User(etName, etAge, etHeight, etWeight, etGender, etPassword,
                        etNumberOfHouseHoldMember, etHouseHoldMkeup, etEmail, etPassword);

                Signup.userAppDb.userDao().insertUser(user);
                Toast.makeText(getApplicationContext(),"User Added Successfully!", Toast.LENGTH_LONG).show();




                break;
            case R.id.Bcancel:
                startActivity(new Intent(this, Login.class));
                break;
        }
    }
}

