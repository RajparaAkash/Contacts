package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common;

import android.content.Context;
import android.content.SharedPreferences;

import com.contacts.phonecontact.phonebook.dialer.Utils.ConstantsUtils;

import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public class PreferencesManager {
    public static final Companion Companion = new Companion(null);
    private static volatile PreferencesManager sInstance;
    private SharedPreferences.Editor myEdit;
    private SharedPreferences sharedPreferences;

    public PreferencesManager(Context context, DefaultConstructorMarker defaultConstructorMarker) {
        this(context);
    }

    private PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, 0);
        myEdit = sharedPreferences.edit();
        myEdit.apply();
    }

    @JvmStatic
    public static final PreferencesManager getInstance(Context context) {
        return Companion.getInstance(context);
    }


    public final boolean getMissedCall() {
        return sharedPreferences.getBoolean(Constants.MISSEDCALL, true);
    }

    public final boolean getIncoming() {
        return sharedPreferences.getBoolean(Constants.INCOMING, true);
    }

    public final boolean getOutgoing() {
        return sharedPreferences.getBoolean(Constants.OUTGOING, true);
    }


    public final Integer getMuteNotification() {
        return sharedPreferences.getInt(Constants.MUTE_NOTIFICATION, -1);
    }

    public final Long getMuteNotificationTime() {
        return sharedPreferences.getLong(Constants.NOTIFICATION_TIME, 0);
    }

    public final Boolean getMuteNotificationAlways() {
        return sharedPreferences.getBoolean(Constants.NOTIFICATION_ALWAYS, false);
    }

    public final Long getAppUpdateDate() {
        return sharedPreferences.getLong(ConstantsUtils.PREF_APP_UPDATE_DATE, 0);
    }

    public final void setMissedCall(Context context, boolean z) {
        Intrinsics.checkNotNullParameter(context, "context");
        SharedPreferences sharedPreferences2 = context.getSharedPreferences(Constants.SHARED_PREFS, 0);
        this.sharedPreferences = sharedPreferences2;
        SharedPreferences.Editor edit = sharedPreferences2.edit();
        this.myEdit = edit;
        edit.putBoolean(Constants.MISSEDCALL, z);
        this.myEdit.apply();
    }

    public final void setInComing(Context context, boolean z) {
        Intrinsics.checkNotNullParameter(context, "context");
        SharedPreferences sharedPreferences2 = context.getSharedPreferences(Constants.SHARED_PREFS, 0);
        this.sharedPreferences = sharedPreferences2;
        SharedPreferences.Editor edit = sharedPreferences2.edit();
        this.myEdit = edit;
        edit.putBoolean(Constants.INCOMING, z);
        this.myEdit.apply();
    }

    public final void setOutgoing(Context context, boolean z) {
        SharedPreferences sharedPreferences2 = context.getSharedPreferences(Constants.SHARED_PREFS, 0);
        this.sharedPreferences = sharedPreferences2;
        SharedPreferences.Editor edit = sharedPreferences2.edit();
        this.myEdit = edit;
        edit.putBoolean(Constants.OUTGOING, z);
        this.myEdit.apply();
    }


    public final void setMuteNotification(Context context, int i) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, 0);
        myEdit = sharedPreferences.edit();
        myEdit.putInt(Constants.MUTE_NOTIFICATION, i);
        myEdit.apply();
    }

    public final void setMuteNotificationTime(Context context, long j) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, 0);
        myEdit = sharedPreferences.edit();
        myEdit.putLong(Constants.NOTIFICATION_TIME, j);
        myEdit.apply();
    }

    public final void setMuteNotificationAlways(Context context, boolean z) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, 0);
        myEdit = sharedPreferences.edit();
        myEdit.putBoolean(Constants.NOTIFICATION_ALWAYS, z);
        this.myEdit.apply();
    }

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public PreferencesManager getInstance(Context context) {
            if (PreferencesManager.sInstance == null) {
                synchronized (this) {
                    PreferencesManager.sInstance = new PreferencesManager(context, null);
                }
            }
            PreferencesManager preferencesManager = PreferencesManager.sInstance;
            return preferencesManager;
        }
    }

}
