package com.stevecrossin.grocerytracker.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.stevecrossin.grocerytracker.entities.ItemsDao;
import com.stevecrossin.grocerytracker.entities.User;
import com.stevecrossin.grocerytracker.entities.UserDao;
import com.stevecrossin.grocerytracker.screens.Login;

import java.util.ArrayList;
import java.util.List;


public class AppDataRepo {
    private UserDao userDao;
    private ItemsDao itemsDao;

    public AppDataRepo(Context context) {
        userDao = AppDb.getDatabase(context).userDao();
    }

    /**
     * Wrapper method. Perform dao operation to add one recipe to the db.
     */
    public void insertUser(User user) {
        userDao.insertUser(user);
    }

    /**
     * Wrapper method. Perform dao operation to get sign in user from the db.
     */
    public User getSignedUser() {
        return userDao.getSignInUser();
    }

    /**
     * Perform operation to update the login status for the user. If the login status is not true, it will also perform the deleteAllIngredient, deleteAllIntolerance
     * and then pantry.deleteAll operations
     */
    public void updateLoginStatus(int userId, boolean isLogin) {
        userDao.updateLoginStatus(userId, isLogin);
    }

    /**
     * Perform dao operation to get users from Users db.
     */
    public User getUserName(String userName) {
        return userDao.getUser(userName);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
}
