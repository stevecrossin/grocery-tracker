package com.stevecrossin.grocerytracker.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "receipts", indices = {@Index(value = {"receiptID"}, unique = true),
        @Index(value = {"email"})},
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "email",
                childColumns = "email",
                onDelete = CASCADE))
public class Receipt {
    //** Users table structure in room database//

    @PrimaryKey(autoGenerate = true)
    private int receiptID;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "receipt_name")
    private String receiptItemName;

    @ColumnInfo(name = "receipt_file_path")
    private String receiptFilePath;

    @ColumnInfo(name = "receipt_time")
    private String receiptTime;

    public int getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(int receiptID) {
        this.receiptID = receiptID;
    }

    public Receipt(String email, String receiptItemName, String receiptFilePath) {
        this.email = email;
        this.receiptItemName = receiptItemName;
        this.receiptFilePath = receiptFilePath;
        this.receiptTime = DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));
    }

    public String getEmail() {
        return email;
    }

    public String getReceiptItemName() {
        return receiptItemName;
    }

    public void setReceiptItemName(String receiptItemName) {
        this.receiptItemName = receiptItemName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(String receiptTime) {
        this.receiptTime = receiptTime;
    }

    public String getReceiptFilePath() {
        return receiptFilePath;
    }

    public void setReceiptFilePath(String receiptFilePath) {
        this.receiptFilePath = receiptFilePath;
    }
}