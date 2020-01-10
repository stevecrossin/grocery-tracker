package com.stevecrossin.grocerytracker.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.database.AppDataRepo;
import com.stevecrossin.grocerytracker.entities.User;

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
     */

    public void GotoReceipts(View view)
    { Intent intent = new Intent(this, Receipts.class);
        startActivity(intent);
    }

    /**
     * This is Online Survey Button function to link to Survey website. Placeholder URL at present
     */
     public void GoToSurvey(View view) {
         String url = "https://globalobesity.com.au";
         startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
     }

    /**
     * This is About GLOBE button function, to link to About screen.
     */
    public void GoToAbout(View view) {
       Intent intent = new Intent(this, About.class);
       startActivity(intent);
    }

    /** Navigate to FAQ screen */
    public void GotoFAQ(View view) {
        Intent intent = new Intent(this, FaqscreenActivity.class);
        startActivity(intent);
    }

    /**
     * This is an OnClick method that is called when the "Logout" button is clicked in the activity.
     * It will create a new instance of the AppDataRepo and get the signed in user, and their user ID will be updated as per the DB operation, to set isLogin for that user in
     * the DB to "false".
     * <p>
     * Once this is completed, the method will load the Login.class, and then start that activity.
     */
    @SuppressLint("StaticFieldLeak")
    public void logout(View view) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                AppDataRepo repo = new AppDataRepo(MainActivity.this);
                User user = repo.getSignedUser();

                if (user != null && repo !=null) {
                    repo.updateLoginStatus(user.getUserID(), false);
                }
                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(MainActivity.this, Login.class));

            }
        }.execute();
    }

    public void sendFeedbackMail(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "stevecrossin@gmail.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on Grocery Tracker Application");
        intent.putExtra(Intent.EXTRA_TEXT, "What I would like to give feedback about:");
        startActivity(intent);
    }
}
