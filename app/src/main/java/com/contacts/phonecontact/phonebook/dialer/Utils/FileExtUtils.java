package com.contacts.phonecontact.phonebook.dialer.Utils;

import android.content.Context;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class FileExtUtils {
    public static File getCachePhoto(Context context) {
        File file = new File(context.getCacheDir(), "contactCache");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, "contactApp_" + System.currentTimeMillis() + ".jpg");
        try {
            file2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file2;
    }

    public static Uri getPhotoUri$default(Context context, File file, int i, Object obj) {
        if ((i & 1) != 0) {
            file = getCachePhoto(context);
        }
        return getPhotoUri(context, file);
    }

    public static Uri getPhotoUri(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    }

    public static Uri getCachePhotoUri(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    }

    public static Uri getCachePhotoUri$default(Context context, File file, int i, Object obj) {
        if ((i & 1) != 0) {
            file = getCachePhoto(context);
        }
        return getCachePhotoUri(context, file);
    }
}
