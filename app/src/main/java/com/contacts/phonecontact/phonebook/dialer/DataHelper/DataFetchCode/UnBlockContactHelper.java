package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import android.content.ContentValues;
import android.content.Context;
import android.provider.BlockedNumberContract;

import kotlin.text.StringsKt;

public class UnBlockContactHelper {
    private final String contact;
    private final Context mContext;

    public UnBlockContactHelper(Context context, String str) {
        this.mContext = context;
        this.contact = str;
    }

    public void invoke() {
        new ContentValues();
        int delete = mContext.getContentResolver().delete(BlockedNumberContract.BlockedNumbers.CONTENT_URI, "original_number = ?", new String[]{StringsKt.replace(contact, "-", "", false)});
        System.out.println((Object) ("remove from block : " + contact + ' ' + delete));
    }

}
