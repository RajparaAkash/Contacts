package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.repo.ContactRepo;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.CursorExtenUtils;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.MyContactsContentProvider;

public class LoadContactByLookUpHelper {
    private final ContactRepo contactRepo;
    private final String key;
    private final Context mContext;

    public LoadContactByLookUpHelper(Context context, ContactRepo contactRepo2, String str) {
        this.mContext = context;
        this.contactRepo = contactRepo2;
        this.key = str;
    }

    private String[] getContactProjection() {
        return new String[]{"mimetype", "contact_id", "raw_contact_id", "data4", "data2", "data5", "data3", "data6", MyContactsContentProvider.COL_PHOTO_URI, "photo_thumb_uri", "starred", "custom_ringtone", "account_name", "account_type"};
    }

    public Contact invoke() {
        if (mContext.getPackageManager().checkPermission("android.permission.READ_PHONE_STATE", mContext.getPackageName()) != PackageManager.PERMISSION_GRANTED || mContext.getPackageManager().checkPermission("android.permission.WRITE_CONTACTS", mContext.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return parseContactCursor(mContext, "(mimetype = ? OR mimetype = ?) AND lookup = ?", new String[]{"vnd.android.cursor.item/name", "vnd.android.cursor.item/organization", key});
    }

    private Contact parseContactCursor(Context context, String str, String[] strArr) {
        Contact contact;
        Cursor query = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, getContactProjection(), str, strArr, null);
        if (query == null) {
            return null;
        }
        if (query.moveToFirst()) {
            contact = contactRepo.getContactWithId(String.valueOf(CursorExtenUtils.getIntValue(query, "raw_contact_id")));
        } else {
            contact = null;
        }

        if (query != null) {
            query.close();
        }
        return contact;
    }

}
