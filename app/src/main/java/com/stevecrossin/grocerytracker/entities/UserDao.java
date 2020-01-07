package com.stevecrossin.grocerytracker.entities;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    /**
     * Inserts new user record into the database. Conflict strategy is set to fail the insert if the userRecord already exists.
     */
    @Insert()
    void insertUser(User user);

    /**
     * Get all users from the database
     */
    @Query("SELECT * FROM user;")
    List<User> getAllUsers();

}
