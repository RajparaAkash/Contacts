package com.contacts.phonecontact.phonebook.dialer.DataHelper.utils;

import android.database.Cursor;

public class CursorExtenUtils {

    public static String getStringValue(Cursor cursor, String str) {
        return cursor.getString(cursor.getColumnIndexOrThrow(str));
    }

    public static String getStringValueOrNull(Cursor cursor, String str) {
        if (cursor.isNull(cursor.getColumnIndexOrThrow(str))) {
            return null;
        }
        return cursor.getString(cursor.getColumnIndexOrThrow(str));
    }

    public static int getIntValue(Cursor cursor, String str) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(str));
    }

    public static Integer getIntValueOrNull(Cursor cursor, String str) {
        if (cursor.isNull(cursor.getColumnIndexOrThrow(str))) {
            return null;
        }
        return Integer.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(str)));
    }

    public static long getLongValue(Cursor cursor, String str) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(str));
    }

    public static Long getLongValueOrNull(Cursor cursor, String str) {
        if (cursor.isNull(cursor.getColumnIndexOrThrow(str))) {
            return null;
        }
        return Long.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(str)));
    }

    public static byte[] getBlobValue(Cursor cursor, String str) {
        return cursor.getBlob(cursor.getColumnIndexOrThrow(str));
    }

}
