package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.Reminder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyDB {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public MyDB(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        this.dbHelper = databaseHelper;
        this.database = databaseHelper.getWritableDatabase();
    }

    public long addReminder(Reminder reminder) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper._TITLE, reminder.getTitle());
        contentValues.put(DatabaseHelper._TIME, Long.valueOf(reminder.getTime()));
        contentValues.put(DatabaseHelper._COLOR, Integer.valueOf(reminder.getColor()));
        contentValues.put(DatabaseHelper._MOBILE_NUMBER, reminder.getMobileNumber());
        return this.database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
    }

    public ArrayList<Reminder> getReminderList(String str) {
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM Reminder", null);
        ArrayList<Reminder> arrayList = new ArrayList<>();
        if (rawQuery.moveToFirst()) {
            do {
                arrayList.add(new Reminder(rawQuery.getInt(0), rawQuery.getString(1), rawQuery.getLong(2), rawQuery.getInt(3), rawQuery.getString(4)));
            } while (rawQuery.moveToNext());
        }
        rawQuery.close();
        return arrayList;
    }

    public int getLastReminderId() {
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM Reminder", null);
        ArrayList arrayList = new ArrayList();
        if (rawQuery.moveToFirst()) {
            do {
                arrayList.add(new Reminder(rawQuery.getInt(0), rawQuery.getString(1), rawQuery.getLong(2), rawQuery.getInt(3), rawQuery.getString(4)));
            } while (rawQuery.moveToNext());
        }
        rawQuery.close();
        Collections.sort(arrayList, new Comparator<Reminder>() {

            public int compare(Reminder reminder, Reminder reminder2) {
                return reminder2.getId() - reminder.getId();
            }
        });
        if (arrayList.size() > 0) {
            return ((Reminder) arrayList.get(0)).getId();
        }
        return -1;
    }

    public void deleteReminder(Reminder reminder) {
        this.database.delete(DatabaseHelper.TABLE_NAME, "_id=?", new String[]{String.valueOf(reminder.getId())});
    }
}
