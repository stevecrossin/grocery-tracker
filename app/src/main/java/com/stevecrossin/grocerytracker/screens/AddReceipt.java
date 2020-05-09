package com.stevecrossin.grocerytracker.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.database.AppDataRepo;
import com.stevecrossin.grocerytracker.entities.Receipt;
import com.stevecrossin.grocerytracker.entities.User;
import com.stevecrossin.grocerytracker.models.ColesReceipt;
import com.stevecrossin.grocerytracker.models.ReceiptLineItem;
import com.stevecrossin.grocerytracker.models.UserReceipt;
import com.stevecrossin.grocerytracker.models.WoolworthsReceipt;
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
     * Writes PDF to CSV - separates columns with comma, lines with a semicolon.
     */
    private static final String CSV_COLUMN_SEPARATOR = ",";
    private static final String CSV_LINE_SEPARATOR = "~";

    private static void writeToCSV(String[] columnHeader, List<ReceiptLineItem> receiptLineItems, String fileName) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
            StringBuffer headerLine = new StringBuffer();
            headerLine.append(columnHeader[0]);
            headerLine.append(CSV_COLUMN_SEPARATOR);
            headerLine.append(columnHeader[1]);
            headerLine.append(CSV_COLUMN_SEPARATOR);
            headerLine.append(columnHeader[2]);
            headerLine.append(CSV_COLUMN_SEPARATOR);
            headerLine.append(columnHeader[3]);
            bw.write(headerLine.toString());
            bw.newLine();
            for (ReceiptLineItem receiptLineItem : receiptLineItems) {
                StringBuffer oneLine = new StringBuffer();
                oneLine.append(receiptLineItem.itemDescription);
                oneLine.append(CSV_COLUMN_SEPARATOR);
                oneLine.append(receiptLineItem.unitPrice);
                oneLine.append(CSV_COLUMN_SEPARATOR);
                oneLine.append(receiptLineItem.quantity);
                oneLine.append(CSV_COLUMN_SEPARATOR);
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

    /**
     * Actions to perform after user picks a file. If user selected a file, the parseAndUploadPDF method will be called.
     * Otherwise an error will be thrown advising the user they have not selected a file.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                // If the user has successfully picked a pdf...
                EditText ediTextFileName = findViewById(R.id.editTextFileName);
                final String fileAlias = ediTextFileName.getText().toString();
                // Process the picked pdf in a background thread.
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Provide indication to the user that the pdf file is being processed.
                        setInProgress();
                        // First, write the pdf file to a local temp storage. This is done because, iTextPdf library takes
                        // a file path as input and not a Uri and the result of the pick operation has a Uri which has to
                        // be read through a ContentResolver first so that it goes through proper permissions first.
                        String pdfFilePath = writePDFToTempStorage(data.getData());
                        if (pdfFilePath == null) {
                            // If something went wrong with writing a copy of the pdf to local storage,
                            // indicate that to the user.
                            setFailure();
                            return;
                        }
                        if (!parseAndUploadPDF(pdfFilePath, fileAlias)) {
                            // If something went wrong with parsing the pdf, indicate that to the user.
                            setFailure();
                            return;
                        }
                        // Clear the temp storage which will have copy of the picked pdf.
                        purgeTempStorage();
                        // Indicate to the user that the pdf file they picked was processed successfully.
                        setSuccess();
                    }
                });
            } else {
                Toast.makeText(this, "Invalid URI", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /***
     * These three methods, setInProgress, setSuccess and setFailure are called post the onActivityResult above.
     * While the inProgress is occurring, all the background processes are working that will be converting the PDF to CSV, extracing receipt items and inserting into the database. This is usually a quick operation.
     * If this process is successful and error free, setSuccess will be shown which changes the content of the TextView accordingly.
     * setFailure will appear if the conversion process is unsuccessful.
     *
     * Note for future developers - in a tiny minority of cases (less than 1%) - if the end user has forwarded their email receipt repeatedly across different email clients and retained all email bodies,
     * and then only if due to a single receipt line item being split across multiple pages without any price/qty/any other integers before the next receipt item appearing, it can cause an error in parsing in the parseReceipt method,
     * causing an IndexOutOfBounds exception in the method. This will be documented in the technical documentation.
     */

    private void setInProgress() {
        // Indicate progress to the user in foreground thread.
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                textViewStatus.setText("Converting pdf to csv...");
            }
        });
    }

    private void setSuccess() {
        // Indicate success to the user in foreground thread.
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
        // Indicate failure to the user in foreground thread.
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
     * Deletes all files in externalFilesDir/Temp directory which is used to write a copy of the
     * picked pdf for processing it into csv.
     */
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

    /**
     * Creates a copy of the pdf file picked by the user in local storage for further processing it
     * into csv.
     *
     * @param pdfUri Uri of the pdf file provided by the pick pdf user operation.
     * @return the file path of the copy of pdf
     */
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

    /**
     * Reads the receipt pdf and parses it to csv, writes to local storage and creates an entry for it
     * in Room DB associating it to the current user.
     *
     * @param pdfFilePath Path to the copy of pdf file in local storage.
     * @param fileAlias   The name of the receipt specified by the user when adding a receipt.
     * @return result - the parsedText of all the receiptText in a string format, as well as the alias of the file.
     */
    private boolean parseAndUploadPDF(final String pdfFilePath, final String fileAlias) {
        try {
            String parsedText = "";
            PdfReader reader = new PdfReader(pdfFilePath);
            int n = reader.getNumberOfPages();
            // Read all pages in the receipt pdf and collect them into a single string.
            for (int i = 0; i < n; i++) {
                parsedText = parsedText + PdfTextExtractor.getTextFromPage(reader, i + 1).replace(" \n", "\n").trim() + "\n"; //Extracting the content from the different pages
            }
            boolean result = parseReceiptPdf(parsedText, fileAlias);
            reader.close();
            return result;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parses the text read from the receipt pdf, writes csv and creates receipt db entry.
     *
     * @param parsedText String containing the text of the entire receipt pdf.
     * @param fileAlias  The name of the receipt specified by the user when adding a receipt.
     * @return
     */
    private boolean parseReceiptPdf(String parsedText, String fileAlias) {
        List<ReceiptLineItem> receiptLineItems;
        String headerText;

        // If contains Woolworths -> do Woolworths or do Coles - first case will handle Coles receipts, other returns Woolworths
        if (!parsedText.contains("Woolworths")) {
            ColesReceipt colesReceipt = new ColesReceipt(parsedText);
            receiptLineItems = colesReceipt.Parse();
            headerText = "Item Description: Unit Price: Quantity: Price";
        }
        //Do Woolworths
        else {
            WoolworthsReceipt woolwothsReceipt = new WoolworthsReceipt(parsedText);
            receiptLineItems = woolwothsReceipt.Parse();
            headerText = "Item Description: Unit Price: Quantity: Price";
        }

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
        Receipt savedReceipt = uploadCSVFileToRoomDB(receiptCSVFilename, fileAlias);

        // push to firebase when you push to RoomDB
        writeCSVFileToFirebase(savedReceipt, headerText.split(": "), receiptLineItems);
        return true;
    }

    /**
     * // RG TBA - need to get the actual items - not just the filename, email of user etc.
     **/
    private void writeCSVFileToFirebase(Receipt savedReceipt, String[] columnHeader, List<ReceiptLineItem> receiptLineItems) {
        UserReceipt userReceipt = new UserReceipt();
        AppDataRepo dataRepo = new AppDataRepo(this);
        dataRepo.getSignedUser();
        savedReceipt.setReceiptContents(composeLineItemsToCSVString(columnHeader, receiptLineItems));
        userReceipt.receipt = new Receipt(savedReceipt);
        String id = databaseReference.push().getKey();
        databaseReference.child(id).setValue(userReceipt);
    }

    private String composeLineItemsToCSVString(String[] columnHeader, List<ReceiptLineItem> receiptLineItems) {
        StringBuilder builder = new StringBuilder();
        builder.append(columnHeader[0]);
        builder.append(CSV_COLUMN_SEPARATOR);
        builder.append(columnHeader[1]);
        builder.append(CSV_COLUMN_SEPARATOR);
        builder.append(columnHeader[2]);
        builder.append(CSV_COLUMN_SEPARATOR);
        builder.append(columnHeader[3]);
        builder.append(CSV_LINE_SEPARATOR);

        for (ReceiptLineItem receiptLineItem : receiptLineItems) {
            builder.append(receiptLineItem.itemDescription);
            builder.append(CSV_COLUMN_SEPARATOR);
            builder.append(receiptLineItem.unitPrice);
            builder.append(CSV_COLUMN_SEPARATOR);
            builder.append(receiptLineItem.quantity);
            builder.append(CSV_COLUMN_SEPARATOR);
            builder.append(receiptLineItem.price);
            builder.append(CSV_LINE_SEPARATOR);
        }

        return builder.toString();
    }

    private Receipt uploadCSVFileToRoomDB(final String csvFilename, final String fileAlias) {
        AppDataRepo dataRepo = new AppDataRepo(this);
        User currentUser = dataRepo.getSignedUser();
        Receipt receipt = new Receipt(currentUser.getEmail(), fileAlias, csvFilename);
        dataRepo.insertReceipt(receipt);
        return receipt;
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