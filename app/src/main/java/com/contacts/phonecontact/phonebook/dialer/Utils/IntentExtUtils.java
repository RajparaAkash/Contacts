package com.contacts.phonecontact.phonebook.dialer.Utils;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;

import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.ConstantsKt;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

public class IntentExtUtils {
    public static String getPhoneNumberFromIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        boolean z = true;
        if (extras != null && extras.containsKey(ConstantsKt.KEY_PHONE)) {
            return intent.getStringExtra(ConstantsKt.KEY_PHONE);
        }
        Bundle extras2 = intent.getExtras();
        if (extras2 == null || !extras2.containsKey("data")) {
            z = false;
        }
        if (z) {
            Bundle extras3 = intent.getExtras();
            Intrinsics.checkNotNull(extras3);
            Object obj = extras3.get("data");
            if (obj != null) {
                ArrayList arrayList = obj instanceof ArrayList ? (ArrayList) obj : null;
                Object firstOrNull = arrayList != null ? CollectionsKt.firstOrNull((List) arrayList) : null;
                ContentValues contentValues = firstOrNull instanceof ContentValues ? (ContentValues) firstOrNull : null;
                if (contentValues != null && contentValues.containsKey("data1")) {
                    return contentValues.getAsString("data1");
                }
            }
        }
        return null;
    }
}
