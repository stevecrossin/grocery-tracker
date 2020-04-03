package com.stevecrossin.grocerytracker.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.adapters.ReceiptsAdapter;
import com.stevecrossin.grocerytracker.database.AppDataRepo;
import com.stevecrossin.grocerytracker.entities.Receipt;

import java.util.List;

public class Receipts extends AppCompatActivity {
    private RecyclerView mReceiptsRecyclerView;
    private TextView mEmptyTextView;
    private ProgressBar mProgressBar;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts);

        mReceiptsRecyclerView = findViewById(R.id.RecyclerView_Receipts);
        mEmptyTextView = findViewById(R.id.emptyTextView);
        mProgressBar = findViewById(R.id.progressBar);

        mReceiptsRecyclerView.setVisibility(View.GONE);
        mEmptyTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AppDataRepo dataRepo = new AppDataRepo(Receipts.this);
                final List<Receipt> receipts = dataRepo.getReceiptsForUser(dataRepo.getSignedUser().getEmail());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (receipts == null || receipts.size() == 0) {
                            mEmptyTextView.setVisibility(View.VISIBLE);
                        } else {
                            mReceiptsRecyclerView.setVisibility(View.VISIBLE);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Receipts.this);
                            mReceiptsRecyclerView.setLayoutManager(layoutManager);
                            ReceiptsAdapter adapter = new ReceiptsAdapter(receipts);
                            mReceiptsRecyclerView.setAdapter(adapter);
                        }

                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}