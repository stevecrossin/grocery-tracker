package com.globe.grocerytracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.globe.grocerytracker.R;
import com.globe.grocerytracker.entities.Receipt;
import com.globe.grocerytracker.screens.ViewReceiptActivity;

import java.util.List;

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.ReceiptsViewHolder> {
    private List<Receipt> mReceipts;

    public ReceiptsAdapter(List<Receipt> receipts) {
        mReceipts = receipts;
    }

    @NonNull
    @Override
    public ReceiptsViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        ConstraintLayout container = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_receipts, parent, false);
        return new ReceiptsViewHolder(container);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReceiptsViewHolder holder, final int position) {
        holder.mTextViewReceiptName.setText(mReceipts.get(position).getReceiptItemName());
        holder.mTextViewReceiptTime.setText(mReceipts.get(position).getReceiptTime());
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = holder.mContainer.getContext();
                context.startActivity(
                        ViewReceiptActivity.createIntent(context, mReceipts.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReceipts == null ? 0 : mReceipts.size();
    }

    public static class ReceiptsViewHolder extends RecyclerView.ViewHolder {
        public View mContainer;
        public TextView mTextViewReceiptName;
        public TextView mTextViewReceiptTime;

        public ReceiptsViewHolder(@NonNull View container) {
            super(container);
            mContainer = container;
            mTextViewReceiptName = container.findViewById(R.id.TextView_Receipt_Name);
            mTextViewReceiptTime = container.findViewById(R.id.TextView_Receipt_Time);
        }
    }
}