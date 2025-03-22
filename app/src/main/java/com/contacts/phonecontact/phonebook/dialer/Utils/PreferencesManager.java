package com.contacts.phonecontact.phonebook.dialer.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.content.ContextCompat;

public class PreferencesManager {
    public static SharedPreferences sp;
    private static PreferencesManager mInstance;
    Context context;

    public PreferencesManager(Context context2) {
        this.context = context2;
        sp = context2.getSharedPreferences(ConstantsUtils.PREF_TAG, 0);
    }

    public static PreferencesManager getInstance(Context context2) {
        if (mInstance == null) {
            synchronized (PreferencesManager.class) {
                if (mInstance == null) {
                    mInstance = new PreferencesManager(context2.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    public static boolean hasPermission(Context context2, String[] strArr) {
        for (String str : strArr) {
            if (ContextCompat.checkSelfPermission(context2, str) != 0) {
                return false;
            }
        }
        return true;
    }


}
