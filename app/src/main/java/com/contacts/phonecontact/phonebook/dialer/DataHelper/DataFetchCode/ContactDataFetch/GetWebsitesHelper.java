package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.SparseArray;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.CursorExtenUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.BasicUtils;
import java.util.ArrayList;

public class GetWebsitesHelper {

    public static SparseArray invoke$default(GetWebsitesHelper getWebsitesUseCase, Context context, Integer num, ArrayList arrayList, int i, Object obj) {
        if ((i & 2) != 0) {
            num = null;
        }
        if ((i & 4) != 0) {
            arrayList = null;
        }
        return getWebsitesUseCase.invoke(context, num, arrayList);
    }

    public SparseArray<ArrayList<String>> invoke(Context context, Integer num, ArrayList<String> arrayList) {
        String str;
        String[] strArr;
        SparseArray<ArrayList<String>> sparseArray = new SparseArray<>();
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String[] strArr2 = {"raw_contact_id", "data1"};
        if (num == null) {
            Integer valueOf = arrayList != null ? arrayList.size() : null;
            str = BasicUtils.getSourcesSelection$default(true, false, false, valueOf, 6, null);
        } else {
            str = "raw_contact_id = ?";
        }
        if (num == null) {
            strArr = BasicUtils.getSourcesSelectionArgs1$default("vnd.android.cursor.item/website", null, arrayList, 2, null);
        } else {
            strArr = new String[]{num.toString()};
        }
        try {
            Cursor query = context.getContentResolver().query(uri, strArr2, str, strArr, null, null);
            if (query != null) {
                if (query.moveToFirst()) {
                    do {
                        int intValue = CursorExtenUtils.getIntValue(query, "raw_contact_id");
                        String stringValue = CursorExtenUtils.getStringValue(query, "data1");
                        if (stringValue == null) {
                            stringValue = "";
                        }
                        if (sparseArray.get(intValue) == null) {
                            sparseArray.put(intValue, new ArrayList<>());
                        }
                        sparseArray.get(intValue).add(stringValue);
                    } while (query.moveToNext());
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sparseArray;
    }

}
