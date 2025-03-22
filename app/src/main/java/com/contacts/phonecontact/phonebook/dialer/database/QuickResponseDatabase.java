package com.contacts.phonecontact.phonebook.dialer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ModelQuickResponse;
import com.contacts.phonecontact.phonebook.dialer.database.dao.QuickResponseDao;

@Database(entities = {ModelQuickResponse.class}, version = 1, exportSchema = false)
public abstract class QuickResponseDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "quick_response";
    private static final Object LOCK = new Object();
    private static volatile QuickResponseDatabase sInstance;

    public static QuickResponseDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = (QuickResponseDatabase) Room.databaseBuilder(context, QuickResponseDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
                }
            }
        }
        return sInstance;
    }

    public abstract QuickResponseDao quickResponseDao();

}
