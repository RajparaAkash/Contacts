package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.provider.BlockedNumberContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

public class BlockContactHelper {
    private final Context mContext;
    private final String phoneNumber;

    public BlockContactHelper(Context context, String str) {
        this.mContext = context;
        this.phoneNumber = str;
    }

    public void invoke() {
        ContentValues contentValues = new ContentValues();
        Log.e("fatal", "invoke: " + phoneNumber );
        contentValues.put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, phoneNumber);
        contentValues.put(BlockedNumberContract.BlockedNumbers.COLUMN_E164_NUMBER, PhoneNumberUtils.normalizeNumber(phoneNumber));
        try {
            mContext.getContentResolver().insert(BlockedNumberContract.BlockedNumbers.CONTENT_URI, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
