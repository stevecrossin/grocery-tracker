package com.stevecrossin.grocerytracker.entities;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

//** Definition of database operations will take place in this file.//

@Dao
public interface ReceiptsDao {

    /**
     * Inserts new receipt record into the database.
     **/
    @Insert()
    void insertReceipt(Receipts receipts);
}
