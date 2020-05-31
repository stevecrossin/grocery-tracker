package com.globe.grocerytracker.entities;

import android.os.Parcel;
import android.os.Parcelable;

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
public class Receipt implements Parcelable {
    //** Users table structure in room database//
    @PrimaryKey(autoGenerate = true)
    private int receiptID;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "receipt_name")
    private String receiptItemName;

    @ColumnInfo(name = "receipt_file_path")
    private String receiptFilePath;

    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        @Override
        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };

    @ColumnInfo(name = "receipt_time")
    private String receiptTime;
    @ColumnInfo(name = "receipt_contents")
    private String receiptContents;

    protected Receipt(Parcel in) {
        receiptID = in.readInt();
        email = in.readString();
        receiptItemName = in.readString();
        receiptFilePath = in.readString();
        receiptTime = in.readString();
    }

    public int getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(int receiptID) {
        this.receiptID = receiptID;
    }

    public String getEmail() {
        return email;
    }

    public Receipt(String email, String receiptItemName, String receiptFilePath) {
        this.email = email;
        this.receiptItemName = receiptItemName;
        this.receiptFilePath = receiptFilePath;
        this.receiptTime = DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));
    }

    // Used to store user in Firebase DB
    public Receipt(Receipt receipt) {
        this.receiptID = receipt.receiptID;
        this.email = receipt.email;
        this.receiptItemName = receipt.receiptItemName;
        this.receiptContents = receipt.receiptContents;
        this.receiptTime = receipt.receiptTime;
    }


    public String getReceiptItemName() {
        return receiptItemName;
    }

    public void setReceiptItemName(String receiptItemName) {
        this.receiptItemName = receiptItemName;
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

    public String getReceiptContents() {
        return receiptContents;
    }

    public void setReceiptContents(String receiptContents) {
        this.receiptContents = receiptContents;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(receiptID);
        parcel.writeString(email);
        parcel.writeString(receiptItemName);
        parcel.writeString(receiptFilePath);
        parcel.writeString(receiptTime);
    }
}