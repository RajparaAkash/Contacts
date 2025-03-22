package com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogDataModels;

import android.telecom.PhoneAccountHandle;

import androidx.annotation.Keep;

@Keep
public class SIMAccount {
    private final PhoneAccountHandle handle;
    private final int id;
    private final String label;
    private final String phoneNumber;

    public SIMAccount(int id, PhoneAccountHandle phoneAccountHandle, String label, String phoneNumber) {
        this.id = id;
        this.handle = phoneAccountHandle;
        this.label = label;
        this.phoneNumber = phoneNumber;
    }


    public PhoneAccountHandle getHandle() {
        return this.handle;
    }

    public int getId() {
        return this.id;
    }

    public String getLabel() {
        return this.label;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

}
