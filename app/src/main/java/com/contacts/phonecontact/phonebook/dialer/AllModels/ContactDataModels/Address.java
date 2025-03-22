package com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels;

import androidx.annotation.Keep;

@Keep
public class Address {
    private String label;
    private int type;
    private String value;

    public Address(String str, int i, String str2) {
        this.value = str;
        this.type = i;
        this.label = str2;
    }


    public String getLabel() {
        return this.label;
    }

    public void setLabel(String str) {
        this.label = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

}
