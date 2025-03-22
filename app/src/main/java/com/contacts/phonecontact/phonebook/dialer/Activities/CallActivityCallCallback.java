package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.telecom.Call;

import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.CallManagerListener;

public class CallActivityCallCallback implements CallManagerListener {
    final CallActivity this$0;

    CallActivityCallCallback(CallActivity callActivity) {
        this.this$0 = callActivity;
    }

    @Override
    public void onStateChanged() {
        this.this$0.updateState();
    }

    @Override
    public void onPrimaryCallChanged(Call call) {
        this$0.callDurationHandler.removeCallbacks(this$0.updateCallDurationTask);
        this$0.updateCallContactInfo(call);
        this$0.updateState();
    }
}
