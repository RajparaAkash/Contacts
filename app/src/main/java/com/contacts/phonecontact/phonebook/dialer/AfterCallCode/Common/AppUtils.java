package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common;

import android.app.ActivityManager;
import android.content.Context;

public class AppUtils {

    public static boolean isEmptyString(String str) {
        return str == null || str.trim().equals("null") || str.trim().length() <= 0;
    }

    public static String addExtraZero(long j) {
        if (j >= 10) {
            return String.valueOf(j);
        }
        return "0" + String.valueOf(j);
    }

    public static boolean isMyServiceRunning(Class<?> cls, Context context) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
