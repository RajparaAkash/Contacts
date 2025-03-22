package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.SparseArray;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Email;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.CursorExtenUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.BasicUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.TypeGetterUtils;

import java.util.ArrayList;

public class GetEmailsHelper {

    public static SparseArray invoke$default(GetEmailsHelper getEmailsUseCase, Context context, Integer num, ArrayList arrayList, int i, Object obj) {
        if ((i & 2) != 0) {
            num = null;
        }
        if ((i & 4) != 0) {
            arrayList = null;
        }
        return getEmailsUseCase.invoke(context, num, arrayList);
    }

    public SparseArray<ArrayList<Email>> invoke(Context context, Integer num, ArrayList<String> arrayList) {
        String str;
        String[] strArr;
        SparseArray<ArrayList<Email>> sparseArray = new SparseArray<>();
        Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String[] strArr2 = {"raw_contact_id", "data1", "data2", "data3"};
        if (num == null) {
            Integer valueOf = arrayList != null ? arrayList.size() : null;
            str = BasicUtils.getSourcesSelection$default(false, false, false, valueOf, 7, null);
        } else {
            str = "raw_contact_id = ?";
        }
        if (num == null) {
            strArr = BasicUtils.getSourcesSelectionArgs1$default(null, null, arrayList, 3, null);
        } else {
            strArr = new String[]{num.toString()};
        }
        Cursor query = context.getContentResolver().query(uri, strArr2, str, strArr, null, null);
        if (query != null) {
            if (query.moveToFirst()) {
                do {
                    int intValue = CursorExtenUtils.getIntValue(query, "raw_contact_id");
                    String stringValue = CursorExtenUtils.getStringValue(query, "data1");
                    String str2 = "";
                    if (stringValue == null) {
                        stringValue = str2;
                    }
                    int intValue2 = CursorExtenUtils.getIntValue(query, "data2");
                    String stringValue2 = CursorExtenUtils.getStringValue(query, "data3");
                    if (stringValue2 != null) {
                        str2 = stringValue2;
                    }
                    boolean z = sparseArray.size() == 0;
                    if (sparseArray.get(intValue) == null) {
                        sparseArray.put(intValue, new ArrayList<>());
                    }
                    ArrayList<Email> arrayList2 = sparseArray.get(intValue);
                    arrayList2.add(new Email(stringValue, TypeGetterUtils.getOriginalTypeEmailEnum(intValue2), str2, z));
                } while (query.moveToNext());
            }
        }
        if (query != null) {
            query.close();
        }
        return sparseArray;
    }

}
