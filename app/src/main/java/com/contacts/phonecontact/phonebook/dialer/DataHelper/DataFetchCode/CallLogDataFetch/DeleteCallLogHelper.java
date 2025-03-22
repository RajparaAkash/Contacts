package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.CallLogDataFetch;

import android.content.Context;
import android.provider.CallLog;

public class DeleteCallLogHelper {

    public void invoke(Context context, int i) {
        context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, "_id IN (?)", new String[]{String.valueOf(i)});
    }
}
