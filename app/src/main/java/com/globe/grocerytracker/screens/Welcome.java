package com.globe.grocerytracker.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.globe.grocerytracker.R;

public class Welcome extends AppCompatActivity implements View.OnClickListener {

    /**
     * Declare View variables
     */
    Button Bwelcome;
    TextView tvWelcomeMessage;

    /**
     * Initialize and add on listener for the button
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        tvWelcomeMessage = (TextView) findViewById(R.id.tvWelcomeMessage);
        Bwelcome = (Button) findViewById(R.id.Bwelcome);

        Bwelcome.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Bwelcome:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

        }
    }
}