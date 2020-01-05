package com.stevecrossin.grocerytracker.database;

import android.content.Context;

import com.stevecrossin.grocerytracker.entities.ItemsDao;
import com.stevecrossin.grocerytracker.entities.UserDao;
import com.stevecrossin.grocerytracker.screens.Login;

public class AppDataRepo {
    private UserDao userDao;
    private ItemsDao itemsDao;

    public AppDataRepo(Context context) {
        userDao = AppDb.getDatabase(context).userDao();
    }
}
