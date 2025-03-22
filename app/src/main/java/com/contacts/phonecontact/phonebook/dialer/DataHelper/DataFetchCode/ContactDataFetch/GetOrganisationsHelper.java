package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.SparseArray;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Organization;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.CursorExtenUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.BasicUtils;

import java.util.ArrayList;

public class GetOrganisationsHelper {

    public static SparseArray invoke$default(GetOrganisationsHelper getOrganisationsUseCase, Context context, Integer num, ArrayList arrayList, int i, Object obj) {
        if ((i & 2) != 0) {
            num = null;
        }
        if ((i & 4) != 0) {
            arrayList = null;
        }
        return getOrganisationsUseCase.invoke(context, num, arrayList);
    }

    public SparseArray<ArrayList<Organization>> invoke(Context context, Integer num, ArrayList<String> arrayList) {
        String str;
        String[] strArr;
        SparseArray<ArrayList<Organization>> sparseArray = new SparseArray<>();
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String[] strArr2 = {"raw_contact_id", "data1", "data5", "data4"};
        if (num == null) {
            Integer valueOf = arrayList != null ? arrayList.size() : null;
            str = BasicUtils.getSourcesSelection$default(true, false, false, valueOf, 6, null);
        } else {
            str = "raw_contact_id = ?";
        }
        if (num == null) {
            strArr = BasicUtils.getSourcesSelectionArgs1$default("vnd.android.cursor.item/organization", null, arrayList, 2, null);
        } else {
            strArr = new String[]{num.toString()};
        }
        Cursor query = context.getContentResolver().query(uri, strArr2, str, strArr, null, null);
        if (query != null) {
            if (query.moveToFirst()) {
                do {
                    int intValue = CursorExtenUtils.getIntValue(query, "raw_contact_id");
                    String stringValue = CursorExtenUtils.getStringValue(query, "data1");
                    if (stringValue == null) {
                        stringValue = "";
                    }
                    String stringValue2 = CursorExtenUtils.getStringValue(query, "data4");
                    if (stringValue2 == null) {
                        stringValue2 = "";
                    }
                    String stringValue3 = CursorExtenUtils.getStringValue(query, "data5");
                    if (stringValue3 == null) {
                        stringValue3 = "";
                    }


                    if (stringValue.length() > 0) {
//                        if (stringValue2.length() > 0) {
                            Organization organization = new Organization(stringValue, stringValue3, stringValue2);
                            if (sparseArray.get(intValue) == null) {
                                sparseArray.put(intValue, new ArrayList<>());
                            }
                            sparseArray.get(intValue).add(organization);
//                        }
                    }
                } while (query.moveToNext());
            }
        }
        if (query != null) {
            query.close();
        }
        return sparseArray;
    }

}
