package com.contacts.phonecontact.phonebook.dialer.DialerCode;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;
import com.contacts.phonecontact.phonebook.dialer.AllModels.SimpleContact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.CursorExtenUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;

import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;

public class MyContactsContentProvider {
    public static final String COL_ANNIVERSARIES = "anniversaries";
    public static final String COL_BIRTHDAYS = "birthdays";
    public static final String COL_CONTACT_ID = "contact_id";
    public static final String COL_NAME = "name";
    public static final String COL_PHONE_NUMBERS = "phone_numbers";
    public static final String COL_PHOTO_URI = "photo_uri";
    public static final String COL_RAW_ID = "raw_id";
    public static final Companion Companion = new Companion(null);
    public static final String FAVORITES_ONLY = "favorites_only";
    private static final String AUTHORITY = "com.simplemobiletools.commons.contactsprovider";
    private static final Uri CONTACTS_CONTENT_URI = Uri.parse("content://com.simplemobiletools.commons.contactsprovider/contacts");

    @JvmStatic
    public static ArrayList<SimpleContact> getSimpleContacts(Context context, Cursor cursor) {
        return Companion.getSimpleContacts(context, cursor);
    }

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public Uri getCONTACTS_CONTENT_URI() {
            return MyContactsContentProvider.CONTACTS_CONTENT_URI;
        }

        @JvmStatic
        public ArrayList<SimpleContact> getSimpleContacts(Context context, Cursor cursor) {
            ArrayList<SimpleContact> arrayList = new ArrayList<>();
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            int intValue = CursorExtenUtils.getIntValue(cursor, MyContactsContentProvider.COL_RAW_ID);
                            int intValue2 = CursorExtenUtils.getIntValue(cursor, "contact_id");
                            String stringValue = CursorExtenUtils.getStringValue(cursor, "name");
                            String stringValue2 = CursorExtenUtils.getStringValue(cursor, MyContactsContentProvider.COL_PHOTO_URI);
                            String stringValue3 = CursorExtenUtils.getStringValue(cursor, MyContactsContentProvider.COL_PHONE_NUMBERS);
                            String stringValue4 = CursorExtenUtils.getStringValue(cursor, MyContactsContentProvider.COL_BIRTHDAYS);
                            String stringValue5 = CursorExtenUtils.getStringValue(cursor, MyContactsContentProvider.COL_ANNIVERSARIES);
                            ArrayList arrayList2 = (ArrayList) new Gson().fromJson(stringValue3, new MyContactPhoneNumbersToken().getType());
                            if (arrayList2 == null) {
                                arrayList2 = new ArrayList();
                            }
                            Type type = new MyContactStringsToken().getType();
                            ArrayList arrayList3 = (ArrayList) new Gson().fromJson(stringValue4, type);
                            if (arrayList3 == null) {
                                arrayList3 = new ArrayList();
                            }
                            ArrayList arrayList4 = (ArrayList) new Gson().fromJson(stringValue5, type);
                            if (arrayList4 == null) {
                                arrayList4 = new ArrayList();
                            }
                            arrayList.add(new SimpleContact(intValue, intValue2, stringValue, stringValue2, arrayList2, arrayList3, arrayList4));
                        } while (cursor.moveToNext());
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            return arrayList;
        }
    }

}
