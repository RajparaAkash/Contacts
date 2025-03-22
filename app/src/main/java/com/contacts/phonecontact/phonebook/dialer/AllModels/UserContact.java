package com.contacts.phonecontact.phonebook.dialer.AllModels;

import androidx.annotation.Keep;

import kotlin.jvm.internal.DefaultConstructorMarker;

@Keep
public class UserContact {
    private final String contactPhotoThumbUri;
    private final String contactPhotoUri;
    private int contactId;
    private String nameSuffix;

    private String ringtoneUri;

    public UserContact(int i, String str, String str2, String str3) {
        this.contactId = i;
        this.nameSuffix = str;
        this.contactPhotoUri = str2;
        this.contactPhotoThumbUri = str3;
    }

    public UserContact(int i, String str, String str2, String str3, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, str, (i2 & 4) != 0 ? null : str2, (i2 & 8) != 0 ? null : str3);
    }


    public int getContactId() {
        return this.contactId;
    }

    public void setContactId(int i) {
        this.contactId = i;
    }

    public String getNameSuffix() {
        return this.nameSuffix;
    }

    public void setNameSuffix(String str) {
        this.nameSuffix = str;
    }

    public String getContactPhotoUri() {
        return this.contactPhotoUri;
    }

    public String getContactPhotoThumbUri() {
        return this.contactPhotoThumbUri;
    }

    public String getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(String ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }

}
