package com.stevecrossin.grocerytracker.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "receipts", indices = {@Index(value = {"receiptID"}, unique = true)})
public class Receipts {
    //** Receipts table structure in room database//

        @PrimaryKey(autoGenerate = true)
        private int receiptID;

        @ColumnInfo(name = "receipt_iten_name")
        private String receiptItemName;

        @ColumnInfo(name = "receipt_item_qty")
        private String receiptItemQty;

    public int getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(int receiptID) {
        this.receiptID = receiptID;
    }

    public String getReceiptItemName() {
        return receiptItemName;
    }

    public void setReceiptItemName(String receiptItemName) {
        this.receiptItemName = receiptItemName;
    }

    public String getReceiptItemQty() {
        return receiptItemQty;
    }

    public void setReceiptItemQty(String receiptItemQty) {
        this.receiptItemQty = receiptItemQty;
    }

    public Receipts(int receiptID, String receiptItemName, String receiptItemQty) {
        this.receiptID = receiptID;
        this.receiptItemName = receiptItemName;
        this.receiptItemQty = receiptItemQty;
    }
}


