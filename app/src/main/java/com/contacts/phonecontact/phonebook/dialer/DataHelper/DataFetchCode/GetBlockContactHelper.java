package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.BlockedNumberContract;
import android.provider.ContactsContract;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.BlockContact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.CursorExtenUtils;
import com.contacts.phonecontact.phonebook.dialer.types.PhoneNumberType;

import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;

public class GetBlockContactHelper {
    private final Context mContext;

    public GetBlockContactHelper(Context context) {
        this.mContext = context;
    }

    public ArrayList<BlockContact> invoke() {
        String str;
        ArrayList<BlockContact> arrayList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Cursor cursor = mContext.getContentResolver().query(BlockedNumberContract.BlockedNumbers.CONTENT_URI, new String[]{"_id", BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, BlockedNumberContract.BlockedNumbers.COLUMN_E164_NUMBER}, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            String stringValue = CursorExtenUtils.getStringValue(cursor, BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER);
                            if (stringValue == null) {
                                stringValue = "";
                            }
                            String stringValue2 = CursorExtenUtils.getStringValue(cursor, BlockedNumberContract.BlockedNumbers.COLUMN_E164_NUMBER);
                            String str2 = stringValue2 == null ? stringValue : stringValue2;
                            Cursor query2 = mContext.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(stringValue)), new String[]{"display_name"}, null, null, null);
                            if (query2.moveToFirst()) {
                                String string2 = query2.getString(query2.getColumnIndexOrThrow("display_name"));
                                str = string2;
                            } else {
                                str = mContext.getString(R.string.unknown);
                            }
                            BlockContact blockContact = new BlockContact(stringValue, str, PhoneNumberType.OTHER, str2, str2, null, 32, null);
                            if (!Intrinsics.areEqual(stringValue, "")) {
                                arrayList.add(blockContact);
                            }
                        } while (cursor.moveToNext());
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

}
