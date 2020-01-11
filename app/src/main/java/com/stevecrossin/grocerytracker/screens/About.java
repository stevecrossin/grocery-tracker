package com.stevecrossin.grocerytracker.screens;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stevecrossin.grocerytracker.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class About extends AppCompatActivity {

    /**
     * On creation of the activity, perform these functions.
     * Set the current view as the activity_about XML and load the UI elements in that XML file into that view, being the TextView box.
     * <p>
     * Using a TextInputStream, the application will read the contents of the about text file and while the contents are not equal to -1,
     * it will take the contents and write them to a buffer in memory. Once this is complete. the output will be converted to a string and then
     * the AboutText UI element will be updated with the contents of this string
     * <p>
     * If the file for some reason is missing, the exception is caught and logged
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView aboutText = findViewById(R.id.faqTextView);
        InputStream textInputStream = getResources().openRawResource(R.raw.about);
        ByteArrayOutputStream textOutputStream = new ByteArrayOutputStream();

        int aboutTextContents;
        try {
            aboutTextContents = textInputStream.read();
            while (aboutTextContents != -1) {
                textOutputStream.write(aboutTextContents);
                aboutTextContents = textInputStream.read();
            }
            textInputStream.close();
        } catch (IOException ioex) {
            Toast.makeText(About.this, "About text file does not exist", Toast.LENGTH_SHORT).show();
        }
        aboutText.setText(textOutputStream.toString());
    }
}
