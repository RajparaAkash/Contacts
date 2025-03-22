package com.contacts.phonecontact.phonebook.dialer.DialerCode.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.contacts.phonecontact.phonebook.dialer.DialerCode.service.SimpleCallScreeningService;

public class CallStateReceiver extends BroadcastReceiver {

    public static boolean isShowCaller = false;
    public static String phoneNumber = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && "android.intent.action.PHONE_STATE".equals(intent.getAction())) {
            handlePhoneState(intent);
        }
    }

    private void handlePhoneState(Intent intent) {
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            }

            if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
                SimpleCallScreeningService.callerIdPopup.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
