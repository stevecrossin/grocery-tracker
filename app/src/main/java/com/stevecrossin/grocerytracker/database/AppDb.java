package com.stevecrossin.grocerytracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.stevecrossin.grocerytracker.entities.Receipt;
import com.stevecrossin.grocerytracker.entities.ReceiptsDao;
import com.stevecrossin.grocerytracker.entities.User;
import com.stevecrossin.grocerytracker.entities.UserDao;

/**
 * Declaration of entities that that exist in the Room Database, and the version number of the database. This version
 * number needs to be updated every
 * time the underlying code for database entities is modified, or the application
 * may crash
 */
@Database(entities = {Receipt.class, User.class}, version = 9, exportSchema = false)
public abstract class AppDb extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract ReceiptsDao itemsDao();

    private static AppDb INSTANCE;

    /**
     * Run room DB as singluar instance. This ensures the ability to access Room as a single instance throughout the lifespan of the application.
     */
    static AppDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDb.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}