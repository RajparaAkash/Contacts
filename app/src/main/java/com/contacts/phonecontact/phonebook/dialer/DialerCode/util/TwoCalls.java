package com.contacts.phonecontact.phonebook.dialer.DialerCode.util;

import android.telecom.Call;

public class TwoCalls extends PhoneState {
    private final Call active;
    private final Call onHold;

    public TwoCalls(Call call, Call call2) {
        super(null);
        this.active = call;
        this.onHold = call2;
    }

    public  Call getActive() {
        return this.active;
    }

    public  Call getOnHold() {
        return this.onHold;
    }
}
