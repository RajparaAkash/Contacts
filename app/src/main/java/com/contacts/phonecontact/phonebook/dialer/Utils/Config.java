package com.contacts.phonecontact.phonebook.dialer.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import kotlin.jvm.internal.DefaultConstructorMarker;

public class Config {
    public static final Companion Companion = new Companion(null);
    private final SharedPreferences prefs;

    public Config(Context context) {
        this.prefs = ContaxtExtUtils.getContactAppPreference(context);
    }


    public SharedPreferences getPrefs() {
        return this.prefs;
    }

    public void saveCustomSIM(String str, String str2) {
        SharedPreferences.Editor edit = this.prefs.edit();
        edit.putString(ConstantsUtils.REMEMBER_SIM_PREFIX + str, Uri.encode(str2)).apply();
    }

    public String getCustomSIM(String str) {
        SharedPreferences sharedPreferences = this.prefs;
        return sharedPreferences.getString(ConstantsUtils.REMEMBER_SIM_PREFIX + str, "");
    }

    public void removeCustomSIM(String str) {
        SharedPreferences.Editor edit = this.prefs.edit();
        edit.remove(ConstantsUtils.REMEMBER_SIM_PREFIX + str).apply();
    }

    public boolean getDisableProximitySensor() {
        return this.prefs.getBoolean(ConstantsUtils.DISABLE_PROXIMITY_SENSOR, false);
    }

    public void setDisableProximitySensor(boolean z) {
        this.prefs.edit().putBoolean(ConstantsUtils.DISABLE_PROXIMITY_SENSOR, z).apply();
    }


    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Config newInstance(Context context) {
            return new Config(context);
        }
    }

}
