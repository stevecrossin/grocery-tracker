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
     NOTE! This code is depreceated.
     * Method that performs the uploading of the file to Firebase.
     * The progress bar that says "Uploading" will appear, and it wil perform the operation to put the file into Firebase, and append with the PDF extension.
     * After the upload is successfully completed, the status textview will be updated with a success message, the filename box will be cleared, and the uploading bar will disappear.
     * <p>
     * Similar operations will happen if file upload fails, but the message to the end user will differ.
     *//*
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
    }*/
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
     * @return
     */
    private boolean parseAndUploadPDF(final String pdfFilePath, final String fileAlias) {
        try {
            String parsedText = "";
            PdfReader reader = new PdfReader(pdfFilePath);
            int n = reader.getNumberOfPages();
            // Read all pages in the receipt pdf and collect them into a single string.
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

    /**
     * Parses items in a receipt.
     *
     * @param receipt String containing the receipt items.
     * @return
     */
    private List<ReceiptLineItem> parseReceipt(String receipt) {
        String[] lines = receipt.split("\n");
        List<String> prunedLines = pruneLines(lines);
        int i = 0;
        List<String> receiptLines = new ArrayList<>();
        while (i < prunedLines.size()) {
            String prunedLine = prunedLines.get(i).trim();
            // If a line ends with a float, that means it is a single line item and so can be processed.
            if (endsWithFloat(prunedLine)) {
                receiptLines.add(prunedLine);
                i++;
            } else {
                // If the next line contains only integers then it is part of a 2 line item. The next to
                // next line should be appended to the current one as they form the item name. The next
                // should then be appended to the end of the item name as that will contain unit price,
                // quantity and price information. This will make it like the single line item.
                if (isOnlyIntegers(prunedLines.get(i + 1))) {
                    receiptLines.add(prunedLine + " " + prunedLines.get(i + 2).trim() + " " + prunedLines.get(i + 1).trim());
                    i += 3;
                } else {
                    // If the next line is not all integers, then it is a 3 or more lines item. In this case
                    // we collect consecutive lines till we hit a line that does not end with a space character.
                    List<String> itemLines = new ArrayList<>();
                    itemLines.add(prunedLines.get(i));
                    itemLines.add(prunedLines.get(i + 1));
                    itemLines.add(prunedLines.get(i + 2));
                    /*int j = i;
                    while (j < prunedLines.size()) {
                        itemLines.add(prunedLines.get(j).trim());
                        if (!prunedLines.get(j).endsWith(" ")) {
                            break;
                        }
                        j++;
                    }*/
                    // Then we process the collected lines of a 3 or more lines item into a single line item.
                    receiptLines.add(processItemLines(itemLines));
                    i = i + 3;
                }
            }
        }

        return processReceiptLines(receiptLines);
    }

    /**
     * Collects the segmented item name in multiple lines into a single line and apeends the unit price, quantity and
     * price details at the end, creating a single line item.
     *
     * @param itemLines Lines to be processed.
     * @return Single line receipt item.
     */
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

    /**
     * Returns true if the String ends with a period character followed by 2 integers.
     *
     * @param line The receipt line.
     * @return true if ends with float. false otherwise.
     */
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

    /**
     * Checks if the String consists only integers ignoring space and period character.
     *
     * @param text The receipt line.
     * @return true if all are integers. false otherwise.
     */
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

    /**
     * Parses a single line receipt item String into ReceiptLineItem
     *
     * @param lineItem Single line receipt item String.
     * @return Processed ReceiptLineItem object.
     */
    private ReceiptLineItem parseLineItem(String lineItem) {
        // Split by space character.
        String[] columns = lineItem.split(" ");
        ReceiptLineItem receiptLineItem = new ReceiptLineItem();
        // The last three entries will be unit price, quantity and price.
        receiptLineItem.price = Float.parseFloat(columns[columns.length - 1]);
        receiptLineItem.quantity = Float.parseFloat(columns[columns.length - 2]);
        receiptLineItem.unitPrice = Float.parseFloat(columns[columns.length - 3]);

        // The rest of the bits will form the item name, so piece them all together before
        // putting them in ReceiptLineItem.
        receiptLineItem.itemDescription = "";
        for (int i = 0; i < columns.length - 3; i++) {
            receiptLineItem.itemDescription = receiptLineItem.itemDescription + " ";
            receiptLineItem.itemDescription = receiptLineItem.itemDescription + columns[i];
        }
        receiptLineItem.itemDescription = receiptLineItem.itemDescription.trim();
        return receiptLineItem;
    }

    /**
     * Process a list of single line receipt item Strings.
     *
     * @param receiptLines List of single line receipt item Strings.
     * @return List of ReceiptLineItems
     */
    private List<ReceiptLineItem> processReceiptLines(List<String> receiptLines) {
        List<ReceiptLineItem> receiptLineItems = new ArrayList<>();
        for (String receiptLine : receiptLines) {
            receiptLineItems.add(parseLineItem(receiptLine));
        }

        return receiptLineItems;
    }

    /**
     * Discards empty lines in a set of lines.
     *
     * @param lines A set of lines
     * @return List of lines containing no empty lines.
     */
    private List<String> pruneLines(String[] lines) {
        List<String> prunedLines = new ArrayList<>();
        for (String line : lines) {
            if (!line.startsWith(" ")) {
                prunedLines.add(line);
            }
        }
        return prunedLines;
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
            headerText = parsedText.substring(0, parsedText.lastIndexOf("Price:") + 6);
            headerText = headerText.substring(headerText.lastIndexOf("\n") + 1);
            String tableText = parsedText.substring(parsedText.lastIndexOf("Price:") + 6, parsedText.indexOf("Subtotal")).trim();
            Log.i("info", headerText);
            receiptLineItems = parseReceipt(tableText);
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
        User currentUser = dataRepo.getSignedUser();
        //userReceipt.user = new User(currentUser.getEmail()); //depreceated, as we don't need user data here anymore.
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