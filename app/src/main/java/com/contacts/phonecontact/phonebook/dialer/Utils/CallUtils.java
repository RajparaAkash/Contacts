package com.contacts.phonecontact.phonebook.dialer.Utils;

import android.os.Build;
import android.telecom.Call;

import kotlin.collections.ArraysKt;

public class CallUtils {
    private static final Integer[] OUTGOING_CALL_STATES = {9, 1, 8};

    public static int getStateCompat(Call call) {
        if (call == null) {
            return 7;
        }
        if (isSPlus()) {
            return call.getDetails().getState();
        }
        return call.getState();
    }

    public static int getCallDuration(Call call) {
        if (call == null) {
            return 0;
        }
        long connectTimeMillis = call.getDetails().getConnectTimeMillis();
        if (connectTimeMillis == 0) {
            return 0;
        }
        return (int) ((System.currentTimeMillis() - connectTimeMillis) / ((long) 1000));
    }

    public static boolean isOutgoing(Call call) {
        if (!ContaxtExtUtils.isQPlus()) {
            return ArraysKt.contains(OUTGOING_CALL_STATES, Integer.valueOf(getStateCompat(call)));
        }
        if (call.getDetails().getCallDirection() == Call.Details.DIRECTION_OUTGOING) {
            return true;
        }
        return false;
    }

    public static boolean hasCapability(Call call, int i) {
        return (call.getDetails().getCallCapabilities() & i) != 0;
    }

    public static boolean isConference(Call call) {
        Call.Details details;
        return (call == null || (details = call.getDetails()) == null || !details.hasProperty(1)) ? false : true;
    }

    public static boolean isNougatPlus() {
        return Build.VERSION.SDK_INT >= 24;
    }

    public static boolean isNougatMR1Plus() {
        return Build.VERSION.SDK_INT >= 25;
    }

    public static boolean isOreoPlus() {
        return Build.VERSION.SDK_INT >= 26;
    }

    public static boolean isOreoMr1Plus() {
        return Build.VERSION.SDK_INT >= 27;
    }

    public static boolean isPiePlus() {
        return Build.VERSION.SDK_INT >= 28;
    }

    public static boolean isRPlus() {
        return Build.VERSION.SDK_INT >= 30;
    }

    public static boolean isSPlus() {
        return Build.VERSION.SDK_INT >= 31;
    }

}
