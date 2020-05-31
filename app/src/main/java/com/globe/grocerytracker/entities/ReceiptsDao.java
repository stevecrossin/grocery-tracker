package com.globe.grocerytracker.entities;

import androidx.room.Dao;
import androidx.room.Insert;

import androidx.room.Query;

import java.util.List;

//** Definition of database operations will take place in this file.//

@Dao
public interface ReceiptsDao {

    /**
     * Inserts new receipt record into the database.
     **/
    @Insert()
    void insertReceipt(Receipt receipts);

    /**
     * Gets all the receipt records for a given user using their email.
     **/
    @Query("SELECT * FROM receipts WHERE email = :email")
    List<Receipt> getReceiptsForUser(String email);
}
