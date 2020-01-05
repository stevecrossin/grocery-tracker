package com.stevecrossin.grocerytracker.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import javax.mail.*;
import android.content.ComponentName;
import android.net.Uri;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.smailnet.eamil.Callback.GetSendCallback;
import com.smailnet.eamil.EmailConfig;
import com.smailnet.eamil.EmailSendClient;
import com.stevecrossin.grocerytracker.R;

public class Feedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        //make sure the socket could be accessed
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
    }
    //Click the cancel button could switch to the main page
    public void switchToMainPage (View view)
    {
        Intent intent = new Intent ( this,MainActivity.class);
        startActivity(intent);
    }
    public void sendMali(View view){
        EditText inputFeedback = (EditText) findViewById(R.id.feedback);
        String  Feedback=inputFeedback.getText().toString();
        // establish the Intent object
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        Uri uri = Uri.parse("mailto:");
        intent.setData(uri);
        // set email address of the receiver
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ "tanchen@deakin.edu.au","lixingh@deakin.edu.au" });
        //set the title
        intent.putExtra(Intent.EXTRA_SUBJECT, "feedback");
        //set the email content
        intent.putExtra(Intent.EXTRA_TEXT, Feedback);
        ComponentName componentName = intent.resolveActivity(getPackageManager());
        if(componentName != null){
            startActivity(intent);
        }
    }
    public void sendFeedbackMail (View view) throws MessagingException {
        EditText inputFeedback = (EditText) findViewById(R.id.feedback);
        String  Feedback=inputFeedback.getText().toString();
        // configure mail server
        EmailConfig config = new EmailConfig()
                .setSmtpHost("smtp.qq.com")              //set the sender server address
                .setSmtpPort(465)                               //set the sender 163 mailbox server port 465/994
                .setAccount("461192934@qq.com")        //set the email address of sender
                .setPassword("sgcognigegibbijh");                         //the sender's password

        // send email, and make sure the emailConfig configuration information is correct
        EmailSendClient emailSendClient = new EmailSendClient(config);
        emailSendClient
                .setTo("2845508542@qq.com")               //email address of the receiver
                .setNickname("grocery_tracker user")                   // sets the sender's name
                .setSubject("grocery_tracker feedback")              //set the object of email
                .setText(Feedback)    //put the content that app user write into email content
                .sendAsyn(this, new GetSendCallback() {
                    @Override
                    public void sendSuccess() {
                        Toast.makeText(Feedback.this, "Send successfully", Toast.LENGTH_SHORT).show();
                    }
                    //if email send successfully, then remind the user

                    @Override
                    public void sendFailure(String errorMsg) {
                        Log.e("123", "sendFailure:=="+errorMsg );
                        Toast.makeText(Feedback.this, "Send Failed " + errorMsg , Toast.LENGTH_SHORT).show();
                    }
                    //if email send failed, then remind the user
                });
    }

}
