package com.stevecrossin.grocerytracker.screens;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri; //URL
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

    /**
     * This is Share Receipts Button function to connect to ShareReceipts interface.
     *
     *     public void GotoReceipts(View view) {
     *         Intent intent = new Intent(this, ShareReceipts.class);
     *         startActivity(intent);
     *     }
     */


    /**
     * This is Online Survey Button function to link to Survey website.
     *
     *     public void GoToSurvey(View view) {
     *         Intent intent = new Intent();
     *         intent.setData(Uri.parse("URL")); // URL is the survey website address.
     *         intent.setAction(Intent.ACTION_VIEW);
     *         this.startActivity(intent);    //Launch browser
     *     }
     */


    /**
     * This is About GLOBE button function, to link to GLOBE website.
     * Need to fix for internet issue.
     */
    public void GotoGlobeWeb(View view) {
//        Uri uri = Uri.parse("https://www.google.com");    //Set up a redirected website
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
        Intent intent = new Intent();
        intent.setData(Uri.parse("https://www.google.com"));
        intent.setAction(Intent.ACTION_VIEW);
        this.startActivity(intent);    //Launch browser

    }

    public void sendFeedbackMail(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "stevecrossin@gmail.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on Grocery Tracker Application");
        intent.putExtra(Intent.EXTRA_TEXT, "What I would like to give feedback about:");
        startActivity(Intent.createChooser(intent, "Choose an Email client:"));
    }
}
