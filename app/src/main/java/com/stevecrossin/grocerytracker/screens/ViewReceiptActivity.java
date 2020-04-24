package com.stevecrossin.grocerytracker.screens;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.adapters.ReceiptItemsAdapter;
import com.stevecrossin.grocerytracker.entities.Receipt;
import com.stevecrossin.grocerytracker.models.ReceiptLineItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewReceiptActivity extends AppCompatActivity {
    private static final String EXTRA_RECEIPT = "extra_receipt";
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public static Intent createIntent(Context packageContext, Receipt receipt) {
        Intent intent = new Intent(packageContext, ViewReceiptActivity.class);
        intent.putExtra(EXTRA_RECEIPT, receipt);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_receipt);

        final Receipt receipt = getIntent().getParcelableExtra(EXTRA_RECEIPT);
        if (receipt == null) {
            Toast.makeText(this, "Invalid Receipt", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final ConstraintLayout container = findViewById(R.id.ConstraintLayout_Container);
        final TextView receiptNameTextView = findViewById(R.id.TextView_Receipt_Name);
        receiptNameTextView.setText(receipt.getReceiptItemName());
        final TextView receiptTimeTextView = findViewById(R.id.TextView_Receipt_Time);
        receiptTimeTextView.setText(receipt.getReceiptTime());
        final RecyclerView receiptItemsRecyclerView = findViewById(R.id.RecyclerView_Receipt_Items);
        receiptItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final TextView receiptTotalTextView = findViewById(R.id.TextView_Receipt_Total_Value);
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        container.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File receiptFile = new File(receipt.getReceiptFilePath());
                    Scanner scanner = new Scanner(receiptFile);

                    ReceiptLineItem.Header header = null;
                    if (scanner.hasNextLine()) {
                        header = new ReceiptLineItem.Header(scanner.nextLine());
                    }

                    float total = 0.0f;
                    final List<ReceiptLineItem> receiptLineItems = new ArrayList<>();
                    while (scanner.hasNextLine()) {
                        ReceiptLineItem receiptLineItem = new ReceiptLineItem(scanner.nextLine());
                        receiptLineItems.add(receiptLineItem);
                        total += receiptLineItem.price;
                    }

                    final ReceiptLineItem.Header headerFinal = header;
                    DecimalFormat decimalFormat = new DecimalFormat();
                    decimalFormat.setMaximumFractionDigits(2);
                    decimalFormat.setMinimumFractionDigits(2);
                    final String totalFinal = "$" + decimalFormat.format(total);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ReceiptItemsAdapter receiptItemsAdapter = new ReceiptItemsAdapter(headerFinal, receiptLineItems);
                            receiptItemsRecyclerView.setAdapter(receiptItemsAdapter);
                            receiptTotalTextView.setText(totalFinal);
                            container.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } catch (FileNotFoundException e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ViewReceiptActivity.this, "Receipt not found", Toast.LENGTH_SHORT).show();
                            ViewReceiptActivity.this.finish();
                        }
                    });
                }
            }
        });
    }
}