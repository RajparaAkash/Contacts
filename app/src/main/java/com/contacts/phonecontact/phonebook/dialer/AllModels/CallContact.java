package com.contacts.phonecontact.phonebook.dialer.AllModels;

import androidx.annotation.Keep;

@Keep
public class CallContact {
    private String name;
    private String number;
    private String numberLabel;
    private String photoUri;
    private String ringtoneUri;

    public CallContact(String str, String str2, String str3, String str4) {
        this.name = str;
        this.photoUri = str2;
        this.number = str3;
        this.numberLabel = str4;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String str) {
        this.number = str;
    }

    public String getNumberLabel() {
        return this.numberLabel;
    }

    public void setNumberLabel(String str) {
        this.numberLabel = str;
    }

    public String getPhotoUri() {
        return this.photoUri;
    }

    public void setPhotoUri(String str) {
        this.photoUri = str;
    }

    public String getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(String ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }
}
