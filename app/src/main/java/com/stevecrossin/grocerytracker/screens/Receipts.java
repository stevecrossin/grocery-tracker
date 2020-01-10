package com.stevecrossin.grocerytracker.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class Receipts extends AppCompatActivity {

    Button selectFile, upload;
    TextView notification;
    Uri pdfUri;// uri are actually URLS that are meant for local storage

    FirebaseStorage storage;// use for upload file
    FirebaseDatabase database;// use for store URL of upload file
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        selectFile = findViewById(R.id.selectFile);
        upload = findViewById(R.id.submit);
        notification = findViewById(R.id.notification);

        selectFile.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Receipts.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPdf();
                } else {
                    ActivityCompat.requestPermissions(Receipts.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });
        upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(pdfUri!=null){
                    uploadFile(pdfUri);
                }
                else {
                    Toast.makeText(Receipts.this, "Please select a file", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadFile(Uri pdfUri){

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        final String fileName = System.currentTimeMillis()+"";
        StorageReference storageReference =storage.getReference();
        storageReference.child("Upload").child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url=taskSnapshot.getStorage().getDownloadUrl().toString();// return thr url of upload file
                        DatabaseReference reference = database.getReference();// return the path to root
                        reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    Toast.makeText(Receipts.this,"File successfully uploaded",Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(Receipts.this,"File not successfully uploaded",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Receipts.this,"File not successfully uploaded",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                // track the progress of == "our upload.."
                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectPdf();
        }
        else
            Toast.makeText(Receipts.this,"please provide permission..",Toast.LENGTH_SHORT).show();
    }

    private void selectPdf(){
        //to offer user to selector a file using file manager
        //we will be using an Intent

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //check whether user has selected a file or not
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==86 && resultCode==RESULT_OK && data!=null){
            pdfUri = data.getData(); //get the uri of the selected file.
            notification.setText("A file is selected : "+ data.getData().getLastPathSegment());
        }
        else{
            Toast.makeText(Receipts.this,"Please select a file",Toast.LENGTH_SHORT).show();
        }
    }

    public void Cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}