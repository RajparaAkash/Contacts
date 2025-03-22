package com.contacts.phonecontact.phonebook.dialer.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import kotlin.collections.CollectionsKt;

public class ReadContact {

    public static void deleteNumFromCallLog(Context context, String strNum) {
        try {
            context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.NUMBER + " =?", new String[]{strNum});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getIdWithNumber(Context context, String str) {
        String str2 = "";
        try {
            Cursor query = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), new String[]{"_id"}, null, null, null);
            if (query != null) {
                while (query.moveToNext()) {
                    str2 = query.getString(query.getColumnIndexOrThrow("_id"));
                }
                query.close();
            }
        } catch (SecurityException exception) {
            exception.printStackTrace();
        }
        return str2;
    }


    public static void shareContactList(Activity activity, List<Contact> list) {

        File file = new File(activity.getCacheDir(), "contactCache");
        if (!file.exists()) {
            file.mkdirs();
        }

        if (list != null && !list.isEmpty()) {

            String str;
            if (list.size() == 1) {
                str = ((Contact) CollectionsKt.first((List) list)).getNameToDisplay() + ".vcf";
            } else {
                str = "contacts.vcf";
            }
            File tempFile = new File(file, str);

            String path = null;

            List<String> listOfIds = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                listOfIds.add(String.valueOf(list.get(i).getContactIdSimple()));
            }

            Cursor phones = activity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts._ID + " in (" + TextUtils.join(",", listOfIds) + ")", null, null);
            if (phones.moveToFirst()) {
                for (int i = 0; i < phones.getCount(); i++) {

                    String lookupKey = phones.getString(phones.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY));
                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
                    AssetFileDescriptor fd;

                    try {
                        fd = activity.getContentResolver().openAssetFileDescriptor(uri, "r");
                        FileInputStream fis = fd.createInputStream();
                        byte[] buf = new byte[(int) fd.getDeclaredLength()];
                        fis.read(buf);
                        String VCard = new String(buf);

                        path = tempFile.getAbsolutePath();
                        FileOutputStream mFileOutputStream = new FileOutputStream(path, true);
                        mFileOutputStream.write(VCard.toString().getBytes());

                        phones.moveToNext();

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                Uri cachePhotoUri = FileExtUtils.getCachePhotoUri(activity, new File(path));
                intent.putExtra(Intent.EXTRA_STREAM, cachePhotoUri);
                intent.setType("application/x-vcard");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                activity.startActivity(Intent.createChooser(intent, "Send email"));
            }
        }
    }
}
