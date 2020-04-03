package com.stevecrossin.grocerytracker.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.adapters.ReceiptItemsAdapter;
import com.stevecrossin.grocerytracker.entities.Receipt;
import com.stevecrossin.grocerytracker.models.ReceiptLineItem;

import java.io.File;
import java.io.FileNotFoundException;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_receipt);

        final Receipt receipt = getIntent().getParcelableExtra(EXTRA_RECEIPT);
        if (receipt == null) {
            Toast.makeText(this, "Invalid Receipt", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final RecyclerView receiptItemsRecyclerView = findViewById(R.id.RecyclerView_Receipt_Items);
        receiptItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        receiptItemsRecyclerView.setVisibility(View.GONE);
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

                    final List<ReceiptLineItem> receiptLineItems = new ArrayList<>();
                    while (scanner.hasNextLine()) {
                        receiptLineItems.add(new ReceiptLineItem(scanner.nextLine()));
                    }

                    final ReceiptLineItem.Header headerFinal = header;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ReceiptItemsAdapter receiptItemsAdapter = new ReceiptItemsAdapter(headerFinal, receiptLineItems);
                            receiptItemsRecyclerView.setAdapter(receiptItemsAdapter);
                            receiptItemsRecyclerView.setVisibility(View.VISIBLE);
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