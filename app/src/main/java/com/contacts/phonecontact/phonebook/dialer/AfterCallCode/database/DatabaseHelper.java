package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE = "create table Reminder(_id INTEGER PRIMARY KEY AUTOINCREMENT, _title TEXT NOT NULL, _time LONG, _color Int, _mobileNumber TEXT);";
    static final String DB_NAME = "cdo_custom.DB";
    static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "Reminder";
    public static final String _COLOR = "_color";
    public static final String _ID = "_id";
    public static final String _MOBILE_NUMBER = "_mobileNumber";
    public static final String _TIME = "_time";
    public static final String _TITLE = "_title";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS Reminder");
        onCreate(sQLiteDatabase);
    }
}
