package com.stevecrossin.grocerytracker.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.database.AppDataRepo;
import com.stevecrossin.grocerytracker.entities.Receipt;
import com.stevecrossin.grocerytracker.entities.User;
import com.stevecrossin.grocerytracker.models.ReceiptLineItem;
import com.stevecrossin.grocerytracker.utils.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class AddReceipt extends AppCompatActivity implements View.OnClickListener {

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
    // RG: Adding another database reference for test purposes.
    DatabaseReference databaseReference;

    Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * OnCreate method, set the view as per XML file, get firebase objects and draw all the elements on the screen,
     * and set an OnClick listener on the upload button.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        // RG: Adding another database reference for test purposes.
        databaseReference = FirebaseDatabase.getInstance().getReference("Receipts");

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
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_PDF_CODE);
    }


    /**
     * Actions to perform after user picks a file. If user selected a file, the parseAndUploadPDF method will be called.
     * Otherwise an error will be thrown advising the user they have not selected a file.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                EditText ediTextFileName = findViewById(R.id.editTextFileName);
                final String fileAlias = ediTextFileName.getText().toString();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        setInProgress();
                        String pdfFilePath = writePDFToTempStorage(data.getData());
                        if (pdfFilePath == null) {
                            setFailure();
                            return;
                        }
                        if (!parseAndUploadPDF(pdfFilePath, fileAlias)) {
                            setFailure();
                            return;
                        }
                        purgeTempStorage();
                        setSuccess();
                    }
                });
            } else {
                Toast.makeText(this, "Invalid URI", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setInProgress() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                textViewStatus.setText("Converting pdf to csv...");
            }
        });
    }

    private void setSuccess() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                textViewStatus.setText(R.string.msgUploadSuccess);
                editTextFilename.setText("");
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setFailure() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                textViewStatus.setText(R.string.msgUploadFailed);
                editTextFilename.setText("");
                progressBar.setVisibility(View.GONE);
            }
        });
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

    private void purgeTempStorage() {
        String dirPath = getExternalFilesDir(null).getAbsolutePath()
                + "/Temp";
        File filePath = new File(dirPath);
        if (filePath.isDirectory() && filePath.exists()) {
            String[] files = filePath.list();
            if (files == null) {
                return;
            }
            for (String file : files) {
                new File(filePath, file).delete();
            }
        }
    }

    private String writePDFToTempStorage(Uri pdfUri) {
        if (pdfUri == null) {
            // Throw exception/Error Log
            return null;
        }

        try {
            InputStream inputStream = getContentResolver().openInputStream(pdfUri);
            if (inputStream == null) {
                return null;
            }
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String filePath = getExternalFilesDir(null).getAbsolutePath()
                    + "/Temp";
            File targetFilePath = new File(filePath);
            if (!targetFilePath.exists()) {
                if (!targetFilePath.mkdirs()) {
                    return null;
                }
            }
            String fileName = "receipt_" + System.currentTimeMillis()
                    + ".pdf";
            File targetFile = new File(targetFilePath, fileName);
            OutputStream outputStream = new FileOutputStream(targetFile);
            outputStream.write(buffer);
            return targetFile.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    private static final String CSV_SEPARATOR = ",";

    private static void writeToCSV(String[] columnHeader, List<ReceiptLineItem> receiptLineItems, String fileName) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
            StringBuffer headerLine = new StringBuffer();
            headerLine.append(columnHeader[0]);
            headerLine.append(CSV_SEPARATOR);
            headerLine.append(columnHeader[1]);
            headerLine.append(CSV_SEPARATOR);
            headerLine.append(columnHeader[2]);
            headerLine.append(CSV_SEPARATOR);
            headerLine.append(columnHeader[3]);
            bw.write(headerLine.toString());
            bw.newLine();
            for (ReceiptLineItem receiptLineItem : receiptLineItems) {
                StringBuffer oneLine = new StringBuffer();
                oneLine.append(receiptLineItem.itemDescription);
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(receiptLineItem.unitPrice);
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(receiptLineItem.quantity);
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(receiptLineItem.price);
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (UnsupportedEncodingException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    private List<ReceiptLineItem> parseReceipt(String receipt) {
        String[] lines = receipt.split("\n");
        List<String> prunedLines = pruneLines(lines);
        int i = 0;
        List<String> receiptLines = new ArrayList<>();
        while (i < prunedLines.size()) {
            String prunedLine = prunedLines.get(i).trim();
            if (endsWithFloat(prunedLine)) {
                receiptLines.add(prunedLine);
                i++;
            } else {
                if (isOnlyIntegers(prunedLines.get(i + 1))) {
                    receiptLines.add(prunedLine + " " + prunedLines.get(i + 2).trim() + " " + prunedLines.get(i + 1).trim());
                    i += 3;
                } else {
                    List<String> itemLines = new ArrayList<>();
                    int j = i;
                    while (j < prunedLines.size()) {
                        itemLines.add(prunedLines.get(j).trim());
                        if (!prunedLines.get(j).endsWith(" ")) {
                            break;
                        }
                        j++;
                    }
                    receiptLines.add(processItemLines(itemLines));
                    i = j + 1;
                }
            }
        }

        return processReceiptLines(receiptLines);
    }

    private String processItemLines(List<String> itemLines) {
        String numerics = "";
        StringBuilder builder = new StringBuilder();
        for (String itemLine : itemLines) {
            if (endsWithFloat(itemLine)) {
                ReceiptLineItem item = parseLineItem(itemLine);
                builder.append(item.itemDescription);
                numerics = item.unitPrice + " " + item.quantity + " " + item.price;
            } else {
                builder.append(itemLine);
            }
            builder.append(" ");
        }
        builder.append(numerics);
        return builder.toString().trim();
    }

    private boolean endsWithFloat(String line) {
        char lastChar = line.charAt(line.length() - 1);
        char lastBeforeChar = line.charAt(line.length() - 2);
        char periodCharacter = line.charAt(line.length() - 3);

        if (lastChar >= '0' && lastChar <= '9' && lastBeforeChar >= '0' && lastBeforeChar <= '9'
                && periodCharacter == '.') {
            return true;
        }
        return false;
    }

    private boolean isOnlyIntegers(String text) {
        char[] characters = text.toCharArray();
        for (char c : characters) {
            if (c == '.' || c == ' ') {
                continue;
            }

            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private ReceiptLineItem parseLineItem(String lineItem) {
        String[] columns = lineItem.split(" ");
        ReceiptLineItem receiptLineItem = new ReceiptLineItem();
        receiptLineItem.price = Float.parseFloat(columns[columns.length - 1]);
        receiptLineItem.quantity = Integer.parseInt(columns[columns.length - 2]);
        receiptLineItem.unitPrice = Float.parseFloat(columns[columns.length - 3]);

        receiptLineItem.itemDescription = "";
        for (int i = 0; i < columns.length - 3; i++) {
            receiptLineItem.itemDescription = receiptLineItem.itemDescription + " ";
            receiptLineItem.itemDescription = receiptLineItem.itemDescription + columns[i];
        }
        receiptLineItem.itemDescription = receiptLineItem.itemDescription.trim();
        return receiptLineItem;
    }

    private List<ReceiptLineItem> processReceiptLines(List<String> receiptLines) {
        List<ReceiptLineItem> receiptLineItems = new ArrayList<>();
        for (String receiptLine : receiptLines) {
            receiptLineItems.add(parseLineItem(receiptLine));
        }

        return receiptLineItems;
    }

    private List<String> pruneLines(String[] lines) {
        List<String> prunedLines = new ArrayList<>();
        for (String line : lines) {
            if (!line.startsWith(" ")) {
                prunedLines.add(line);
            }
        }
        return prunedLines;
    }

    private boolean parseAndUploadPDF(final String pdfUriPath, final String fileAlias) {
        try {
            String parsedText = "";
            PdfReader reader = new PdfReader(pdfUriPath);
            int n = reader.getNumberOfPages();
            for (int i = 0; i < n; i++) {
                parsedText = parsedText + PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n"; //Extracting the content from the different pages
            }
            boolean result = parseReceiptPdf(parsedText, fileAlias);
            reader.close();
            return result;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean parseReceiptPdf(String parsedText, String fileAlias) {
        String headerText = parsedText.substring(0, parsedText.lastIndexOf(": \n"));
        headerText = headerText.substring(headerText.lastIndexOf("\n") + 1);
        String tableText = parsedText.substring(parsedText.lastIndexOf(": \n") + 3, parsedText.indexOf("Subtotal")).trim();
        List<ReceiptLineItem> receiptLineItems = parseReceipt(tableText);
        if (receiptLineItems == null || receiptLineItems.size() == 0) {
            return false;
        }

        String filePath = getExternalFilesDir(null).getAbsolutePath()
                + "/Receipt";
        File receiptCSVFilepath = new File(filePath);
        if (!receiptCSVFilepath.exists()) {
            if (!receiptCSVFilepath.mkdirs()) {
                return false;
            }
        }
        String receiptCSVFilename = filePath + "/receipt_" + System.currentTimeMillis() + ".csv";
        writeToCSV(headerText.split(": "), receiptLineItems, receiptCSVFilename);
        uploadCSVFileToRoomDB(receiptCSVFilename, fileAlias);

        // push to firebase when you push to RoomDB
        writeCSVFiletoFirebase(receiptCSVFilename, fileAlias, receiptLineItems);
        return true;
    }

    /**
     // RG TBA - need to get the actual items - not just the filename, email of user etc.

    **/
    private void writeCSVFiletoFirebase(String receiptCSVFilename, String fileAlias, List receiptLineItems) {
        String csvFilename = receiptCSVFilename;
        String fileAlias1 = fileAlias;
        String testText = "Test Text";
        List receiptItems = receiptLineItems;

        if(!TextUtils.isEmpty(csvFilename) && !TextUtils.isEmpty(fileAlias1)){

            AppDataRepo dataRepo = new AppDataRepo(this);
            User currentUser = dataRepo.getSignedUser();

            String id = databaseReference.push().getKey();
            Receipt receipt = new Receipt(currentUser.getEmail(), fileAlias1, csvFilename);
            databaseReference.child(id).setValue(receipt);

        }
        else {
            // TBA
        }
    }

    private void uploadCSVFileToRoomDB(final String csvFilename, final String fileAlias) {
        // TODO: Upload to Room DB.
        AppDataRepo dataRepo = new AppDataRepo(this);
        User currentUser = dataRepo.getSignedUser();
        Receipt receipt = new Receipt(currentUser.getEmail(), fileAlias, csvFilename);
        dataRepo.insertReceipt(receipt);
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