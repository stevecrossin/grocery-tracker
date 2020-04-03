package com.stevecrossin.grocerytracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.entities.Receipt;

import java.util.ArrayList;
import java.util.List;

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.ReceiptsViewHolder> {
    private List<Receipt> mReceipts;

    public ReceiptsAdapter(List<Receipt> receipts) {
        mReceipts = receipts;
    }

    @NonNull
    @Override
    public ReceiptsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout container = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_receipts, parent, false);
        return new ReceiptsViewHolder(container);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptsViewHolder holder, int position) {
        holder.mTextViewReceiptName.setText(mReceipts.get(position).getReceiptItemName());
        holder.mTextViewReceiptTime.setText(mReceipts.get(position).getReceiptTime());
    }

    @Override
    public int getItemCount() {
        return mReceipts == null ? 0 : mReceipts.size();
    }

    public static class ReceiptsViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewReceiptName;
        public TextView mTextViewReceiptTime;

        public ReceiptsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewReceiptName = itemView.findViewById(R.id.TextView_Receipt_Name);
            mTextViewReceiptTime = itemView.findViewById(R.id.TextView_Receipt_Time);
        }
    }
}