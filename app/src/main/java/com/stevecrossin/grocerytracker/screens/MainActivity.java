package com.stevecrossin.grocerytracker.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.stevecrossin.grocerytracker.R;

public class MainActivity extends AppCompatActivity {

    /**
     * Sets the content view to the activity_main layout.
     * This will also initialise any UI elements, and control core behaviour of the main activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void switchToFeedBackPage (View view)
    {
        Intent intent = new Intent ( this,Feedback.class);
        startActivity(intent);
    }
}
