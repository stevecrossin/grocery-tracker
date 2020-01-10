package com.stevecrossin.grocerytracker.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.other.Constants;

import java.text.MessageFormat;

public class Receipts extends AppCompatActivity implements View.OnClickListener {

    /* Picture code for PDF files */
    final static int PICK_PDF_CODE = 2342;

    /**
     * Initialisation of all the elements
     **/
    TextView textViewStatus;
    EditText editTextFilename;
    ProgressBar progressBar;
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;

    /**
     * OnCreate method, set the view as per XML file, get firebase objects and draw all the elements on the screen,
     * and set an OnClick listener on the upload button.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        textViewStatus = findViewById(R.id.textViewStatus);
        editTextFilename = findViewById(R.id.editTextFileName);
        progressBar = findViewById(R.id.progressbar);
        findViewById(R.id.buttonUploadFile).setOnClickListener(this);
    }

    /**
     * Function that will get the PDF from storage.
     */
    private void getPDF() {
        //Intent to open file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_PDF_CODE);
    }

    /**
     * Actions to perform after user picks a file. If user selected a file, the uploadFile method will be called.
     * Otherwise an error will be thrown advising the user they have not selected a file.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data.getData() != null) {
                uploadFile(data.getData());
            } else {
                textViewStatus.setText(R.string.msgNoFile);
            }
        }
    }

    /**
     * Method that performs the uploading of the file to Firebase.
     * The progress bar that says "Uploading" will appear, and it wil perform the operation to put the file into Firebase, and append with the PDF extension.
     * After the upload is successfully completed, the status textview will be updated with a success message, the filename box will be cleared, and the uploading bar will disappear.
     * <p>
     * Similar operations will happen if file upload fails, but the message to the end user will differ.
     */
    private void uploadFile(Uri data) {
        progressBar.setVisibility(View.VISIBLE);
        StorageReference sRef = mStorageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".pdf");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete()) ;
                        Uri url = uri.getResult();
                        textViewStatus.setText(R.string.msgUploadSuccess);
                        editTextFilename.setText("");
                        progressBar.setVisibility(View.GONE);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        textViewStatus.setText(R.string.msgUploadFailed);
                        editTextFilename.setText("");
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        textViewStatus.setText(MessageFormat.format("Uploading...", (int) getProgress(taskSnapshot)));
                    }
                });

    }

    /**
     * Gets the progress of the upload task to the Firebase data storage.
     */
    private double getProgress(UploadTask.TaskSnapshot taskSnapshot) {
        return (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
    }

    /**
     * Handles onClick events. If the user clicks the upload receipt, the getPDF method will be called.
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonUploadFile) {
            getPDF();
        }
    }
}