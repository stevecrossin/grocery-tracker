package com.stevecrossin.grocerytracker.database;

import android.content.Context;

import com.stevecrossin.grocerytracker.entities.ItemsDao;
import com.stevecrossin.grocerytracker.entities.User;
import com.stevecrossin.grocerytracker.entities.UserDao;
import com.stevecrossin.grocerytracker.screens.Login;

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
}
