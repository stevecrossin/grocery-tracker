package com.stevecrossin.grocerytracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.models.ReceiptLineItem;

import java.text.DecimalFormat;
import java.util.List;

public class ReceiptItemsAdapter extends RecyclerView.Adapter<ReceiptItemsAdapter.ReceiptItemsViewHolder> {
    ReceiptLineItem.Header mHeader;
    List<ReceiptLineItem> mReceiptLineItems;

    public static class ReceiptItemsViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewItemDescription;
        TextView mTextViewUnitPriceLabel;
        TextView mTextViewUnitPriceValue;
        TextView mTextViewQuantityLabel;
        TextView mTextViewQuantityValue;
        TextView mTextViewPriceLabel;
        TextView mTextViewPriceValue;

        public ReceiptItemsViewHolder(@NonNull View container) {
            super(container);
            mTextViewItemDescription = container.findViewById(R.id.TextView_Item_Description);
            mTextViewUnitPriceLabel = container.findViewById(R.id.TextView_UnitPrice_Label);
            mTextViewUnitPriceValue = container.findViewById(R.id.TextView_UnitPrice_Value);
            mTextViewQuantityLabel = container.findViewById(R.id.TextView_Quantity_Label);
            mTextViewQuantityValue = container.findViewById(R.id.TextView_Quantity_Value);
            mTextViewPriceLabel = container.findViewById(R.id.TextView_Price_Label);
            mTextViewPriceValue = container.findViewById(R.id.TextView_Price_Value);
        }
    }

    public ReceiptItemsAdapter(ReceiptLineItem.Header header, List<ReceiptLineItem> receiptLineItems) {
        mHeader = header;
        mReceiptLineItems = receiptLineItems;
    }

    @NonNull
    @Override
    public ReceiptItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout container = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_receipt_line_item, parent, false);
        return new ReceiptItemsViewHolder(container);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptItemsViewHolder holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        ReceiptLineItem receiptLineItem = mReceiptLineItems.get(position);
        holder.mTextViewItemDescription.setText(receiptLineItem.itemDescription);
        holder.mTextViewUnitPriceLabel.setText(mHeader.unitPriceHeader);
        holder.mTextViewUnitPriceValue.setText(String.format("$%s", decimalFormat.format(receiptLineItem.unitPrice)));
        holder.mTextViewQuantityLabel.setText(mHeader.quantityHeader);
        holder.mTextViewQuantityValue.setText(String.format(Integer.toString(receiptLineItem.quantity)));
        holder.mTextViewPriceLabel.setText(mHeader.priceHeader);
        holder.mTextViewPriceValue.setText(String.format("$%s", decimalFormat.format(receiptLineItem.price)));
    }

    @Override
    public int getItemCount() {
        return mReceiptLineItems == null ? 0 : mReceiptLineItems.size();
    }
}