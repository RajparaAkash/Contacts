package com.contacts.phonecontact.phonebook.dialer.DialerCode.util;

import android.telecom.Call;

public class SingleCall extends PhoneState {
    private final Call call;

    public SingleCall(Call call2) {
        super(null);
        this.call = call2;
    }

    public Call getCall() {
        return this.call;
    }

}
