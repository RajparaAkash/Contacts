package com.contacts.phonecontact.phonebook.dialer.DialerCode.util;

import android.os.Build;
import android.os.Looper;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

public class ConstantsKt {

    public static final String DATE_FORMAT_FOUR = "dd MMM, yyyy";
    public static final int HOUR_SECONDS = 3600;
    public static final String KEY_PHONE = "phone";
    public static final int LICENSE_APNG = 268435456;
    public static final int LICENSE_AUDIO_RECORD_VIEW = 67108864;
    public static final int LICENSE_EVENT_BUS = 33554432;
    public static final String TIME_FORMAT_12 = "hh:mm a";
    public static final String TIME_FORMAT_24 = "HH:mm";

    public static boolean isOnMainThread() {
        return Intrinsics.areEqual(Looper.myLooper(), Looper.getMainLooper());
    }

    public static void ensureBackgroundThread0(Function0 function0) {
        function0.invoke();
    }


    public static boolean isQPlus() {
        return Build.VERSION.SDK_INT >= 29;
    }

}
