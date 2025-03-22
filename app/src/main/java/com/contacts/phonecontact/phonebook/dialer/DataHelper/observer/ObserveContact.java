package com.contacts.phonecontact.phonebook.dialer.DataHelper.observer;

import android.database.ContentObserver;

import com.contacts.phonecontact.phonebook.dialer.DataHelper.callback.OnContactChange;

public class ObserveContact extends ContentObserver {
    private OnContactChange changesAppear;

    public ObserveContact(OnContactChange onContactChange) {
        super(null);
        this.changesAppear = onContactChange;
    }

    public void onChange(boolean z) {
        super.onChange(z);
        this.changesAppear.onContactChange();
    }

}
