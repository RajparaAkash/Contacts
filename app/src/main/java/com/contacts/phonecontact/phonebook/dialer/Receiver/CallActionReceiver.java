package com.contacts.phonecontact.phonebook.dialer.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.contacts.phonecontact.phonebook.dialer.Activities.CallActivity;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.CallManager;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.Utils.ConstantsUtils;

public class CallActionReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals(ConstantsUtils.DECLINE_CALL)) {
                CallManager.Companion.reject();
            } else if (action.equals(ConstantsUtils.ACCEPT_CALL)) {
                MyApplication.getInstance().isShowingAd = true;
                context.startActivity(CallActivity.Companion.getStartIntent(context));
                CallManager.Companion.accept();
            }
        }
    }
}
