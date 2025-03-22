package com.contacts.phonecontact.phonebook.dialer.DialerCode.service;

import android.telecom.Call;

public class CallServiceCallListener1 extends Call.Callback {
    final CallService callService;

    CallServiceCallListener1(CallService callService) {
        this.callService = callService;
    }

    public void onStateChanged(Call call, int i) {
        super.onStateChanged(call, i);
        if (i != 7) {
            callService.callNotificationManager.setupNotification();
        }
    }
}
