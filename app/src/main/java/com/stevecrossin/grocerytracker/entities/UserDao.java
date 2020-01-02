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

    /**
     * Sets the loginStatus for the user based on the based on the user ID given
     */
    @Query("UPDATE user SET login_status = :isLogin  WHERE userID = :userId;")
    void updateLoginStatus(int userId, boolean isLogin);

    /**
     * Selects all users from the database that are currently logged in
     */
    @Query("SELECT * from user WHERE login_status= 1")
    User getSignInUser();
}
