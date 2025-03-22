package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CustomViewReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals("com.bestrecorder.easyvoicerecorder.audiorecorder.calling.android.intent.SEARCH")) {
            intent.getAction().equals("android.intent.action.PHONE_STATE");
        }
    }
}
