package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import androidx.annotation.Keep;

import java.util.Locale;

@Keep
public class Country {
    private String code;
    private String currency;
    private String dialCode;
    private int flag;
    private String name;

    public Country(String str) {
        this.code = str;
    }

    Country(String str, String str2, String str3, int i, String str4) {
        this.code = str;
        this.name = str2;
        this.dialCode = str3;
        this.flag = i;
        this.currency = str4;
    }

    public Country(String str, String str2, String str3, String str4) {
        this.code = str;
        this.name = str2;
        this.dialCode = str3;
        this.currency = str4;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String str) {
        this.currency = str;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String str) {
        this.code = str;
        if (TextUtils.isEmpty(this.name)) {
            this.name = new Locale("", str).getDisplayName();
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getDialCode() {
        return this.dialCode;
    }

    public void setDialCode(String str) {
        this.dialCode = str;
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int i) {
        this.flag = i;
    }

    public void loadFlagByCode(Context context) {
        if (this.flag == -1) {
            try {
                Resources resources = context.getResources();
                this.flag = resources.getIdentifier("flag_" + this.code.toLowerCase(Locale.ENGLISH), "drawable", context.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
                this.flag = -1;
            }
        }
    }
}
