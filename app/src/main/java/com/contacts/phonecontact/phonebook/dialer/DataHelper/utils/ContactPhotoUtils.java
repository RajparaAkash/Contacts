package com.contacts.phonecontact.phonebook.dialer.DataHelper.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract;

import java.io.ByteArrayOutputStream;

public class ContactPhotoUtils {

    public static byte[] getByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream2);
                byte[] byteArray = byteArrayOutputStream2.toByteArray();
                byteArrayOutputStream2.close();
                return byteArray;
            } catch (Throwable th2) {
                byteArrayOutputStream = byteArrayOutputStream2;
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            }
        } catch (Throwable th3) {
            if (byteArrayOutputStream != null) {
            }
        }
        return null;
    }

    public static int getPhotoThumbnailSize(Context context) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.DisplayPhoto.CONTENT_MAX_DIMENSIONS_URI, new String[]{"thumbnail_max_dim"}, null, null, null);
            boolean z = true;
            if (cursor == null || !cursor.moveToFirst()) {
                z = false;
            }
            if (z) {
                int intValue = CursorExtenUtils.getIntValue(cursor, "thumbnail_max_dim");
                if (cursor != null) {
                    cursor.close();
                }
                return intValue;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
        return 0;
    }

}
