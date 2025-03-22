package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.SparseArray;

import com.contacts.phonecontact.phonebook.dialer.Utils.BasicUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.PreferencesManager;
import com.contacts.phonecontact.phonebook.dialer.Utils.TypeGetterUtils;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.CursorExtenUtils;

import java.util.ArrayList;

public class GetPhoneNumbersHelper {

    public static SparseArray invoke$default(GetPhoneNumbersHelper getPhoneNumbersUseCase, Context context, Integer num, ArrayList arrayList, int i, Object obj) {
        if ((i & 2) != 0) {
            num = null;
        }
        if ((i & 4) != 0) {
            arrayList = null;
        }
        return getPhoneNumbersUseCase.invoke(context, num, arrayList);
    }

    public SparseArray<ArrayList<PhoneNumber>> invoke(final Context context, final Integer n, final ArrayList<String> list) {
        final SparseArray sparseArray = new SparseArray();
        final Uri content_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String sourcesSelection$default;
        if (n == null) {
            Integer value;
            if (list != null) {
                value = list.size();
            } else {
                value = null;
            }
            sourcesSelection$default = BasicUtils.getSourcesSelection$default(false, false, false, (int) value, 7, (Object) null);
        } else {
            sourcesSelection$default = "raw_contact_id = ?";
        }
        String[] sourcesSelectionArgs1$default;
        if (n == null) {
            sourcesSelectionArgs1$default = BasicUtils.getSourcesSelectionArgs1$default((String) null, (Integer) null, (ArrayList) list, 3, (Object) null);
        } else {
            sourcesSelectionArgs1$default = new String[]{n.toString()};
        }
        if (this.isAllPermissionGranted(context)) {
            Cursor cursor = context.getContentResolver().query(content_URI, new String[]{"raw_contact_id", "data1", "data4", "data2", "data3"}, sourcesSelection$default, sourcesSelectionArgs1$default, (String) null);
            try {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            final int intValue = CursorExtenUtils.getIntValue(cursor, "raw_contact_id");
                            String stringValue = CursorExtenUtils.getStringValue(cursor, "data1");
                            if (stringValue == null) {
                                stringValue = "";
                            }
                            String stringValue2 = CursorExtenUtils.getStringValue(cursor, "data4");
                            if (stringValue2 == null) {
                                stringValue2 = "";
                            }
                            final int intValue2 = CursorExtenUtils.getIntValue(cursor, "data2");
                            String stringValue3 = CursorExtenUtils.getStringValue(cursor, "data3");
                            if (stringValue3 == null) {
                                stringValue3 = "";
                            }
                            final PhoneNumber e = new PhoneNumber(stringValue, TypeGetterUtils.getOriginalTypeEnum(intValue2), stringValue3, stringValue2, sparseArray.size() == 0);
                            if (sparseArray.get(intValue) == null) {
                                sparseArray.put(intValue, (Object) new ArrayList());
                            }
                            ((ArrayList) sparseArray.get(intValue)).add(e);
                        } while (cursor.moveToNext());
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return (SparseArray<ArrayList<PhoneNumber>>) sparseArray;
    }

    private boolean isAllPermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return PreferencesManager.hasPermission(context, new String[]{"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS"});
        }
        return true;
    }

}
