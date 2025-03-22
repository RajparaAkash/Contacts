package com.contacts.phonecontact.phonebook.dialer.Activities;

import com.contacts.phonecontact.phonebook.dialer.Utils.AppUtils;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.CallManager;
import com.contacts.phonecontact.phonebook.dialer.Utils.CallUtils;

public class CallActivityUpdateCallDurationTask implements Runnable {
    final CallActivity this$0;

    CallActivityUpdateCallDurationTask(CallActivity callActivity) {
        this.this$0 = callActivity;
    }

    public void run() {
        this$0.callDuration = CallUtils.getCallDuration(CallManager.Companion.getPrimaryCall());
        if (!(this$0.isCallEnded)) {
            this$0.binding.callStatusLabel.setText(AppUtils.getFormattedDuration$default(this$0.callDuration, false, 1, null));
            this$0.callDurationHandler.postDelayed(this, 1000);
        }
    }
}
