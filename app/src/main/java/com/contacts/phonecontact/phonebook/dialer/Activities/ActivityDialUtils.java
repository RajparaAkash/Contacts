package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.content.Context;
import android.os.Build;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class ActivityDialUtils {

    public static List<PhoneAccountHandle> getSimList(Context context) {
        ArrayList arrayList = new ArrayList();
        Object systemService = context.getSystemService(Context.TELECOM_SERVICE);
        TelecomManager telecomManager = (TelecomManager) systemService;
        if (ActivityCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0 || Build.VERSION.SDK_INT < 23) {
            return arrayList;
        }
        List<PhoneAccountHandle> callCapablePhoneAccounts = telecomManager.getCallCapablePhoneAccounts();
        return callCapablePhoneAccounts;
    }

}
