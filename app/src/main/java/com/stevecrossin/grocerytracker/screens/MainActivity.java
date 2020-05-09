package com.stevecrossin.grocerytracker.screens;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.stevecrossin.grocerytracker.BuildConfig;
import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.database.AppDataRepo;
import com.stevecrossin.grocerytracker.entities.User;
import com.stevecrossin.grocerytracker.utils.Reminders;

public class MainActivity extends AppCompatActivity {
    
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseInAppMessaging mInAppMessaging;

    /**
     * Sets the content view to the activity_main layout.
     * This will also initialise any UI elements, and control core behaviour of the main activity.
     * Reminders.getInstance - this is for newly signed up users, and for users
     * using a previous version of the app - we need to schedule the reminder for them.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mInAppMessaging = FirebaseInAppMessaging.getInstance();
        mInAppMessaging.setAutomaticDataCollectionEnabled(true);
        mInAppMessaging.setMessagesSuppressed(false);
        Button remindersDebugButton = findViewById(R.id.reminders_debug);
        if (!BuildConfig.DEBUG) {
            remindersDebugButton.setVisibility(View.GONE);
        }

        // For newly signed up users and existing users from previous version of the app, we need
        // to schedule reminder.
        Reminders.getInstance().scheduleIfNecessary(getApplicationContext());
    }

    /**
     * This is Share Receipts Button function to connect to ShareReceipts interface.
     */

    public void GotoReceipts(View view) {
        mFirebaseAnalytics.logEvent("engagement_party", new Bundle());
        Intent intent = new Intent(this, AddReceipt.class);
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

                if (user != null) {
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

    @SuppressLint("StaticFieldLeak")
    public void receipts(View view) {
        startActivity(new Intent(this, Receipts.class));
    }

    public void remindersDebug(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reminders Debug");

        final View remindersDebugLayout = getLayoutInflater().inflate(R.layout.reminders_debug_dialog, null);
        builder.setView(remindersDebugLayout);

        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences preferences = MainActivity.this.getSharedPreferences("reminders_debug", MODE_PRIVATE);
                EditText editText = remindersDebugLayout.findViewById(R.id.editTextRemindersDebug);
                preferences.edit().putString("reminder_minutes", editText.getText().toString()).apply();
            }
        });

        builder.setNegativeButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences preferences = MainActivity.this.getSharedPreferences("reminders_debug", MODE_PRIVATE);
                preferences.edit().remove("reminder_minutes").apply();
                EditText editText = remindersDebugLayout.findViewById(R.id.editTextRemindersDebug);
                editText.setText("");
            }
        });

        builder.create().show();
        EditText editText = remindersDebugLayout.findViewById(R.id.editTextRemindersDebug);
        editText.setText(getSharedPreferences("reminders_debug", MODE_PRIVATE).getString("reminder_minutes", ""));
    }

    public void sendFeedbackMail(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "stevecrossin@gmail.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on Grocery Tracker Application");
        intent.putExtra(Intent.EXTRA_TEXT, "What I would like to give feedback about:");
        startActivity(intent);
    }

    /**
     * Overrides the back button function on the MainActivity - we don't want them going back to any past screens - clicking back will now just close the app.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();

    }
}