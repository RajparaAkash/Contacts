package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Activity.MainCallActivity;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.Constants;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.PreferencesManager;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;

import java.util.Date;

public class PhoneStateReceiver1 extends BroadcastReceiver {

    public static boolean isIncomingCallEventSend = false;
    public static long callStartTime1 = System.currentTimeMillis();
    static boolean isIncomingCall = false;
    static boolean isMissCall = false;
    static boolean isOutgoingCall = false;
    static boolean isRinging = false;
    static boolean isShowScreen = false;
    static String outgoingSavedNumber;
    protected Context savedContext;
    Date callStartTime = new Date();
    PreferencesManager preferencesManager;

    public void onReceive(Context context, Intent intent) {
        this.preferencesManager = PreferencesManager.getInstance(context);
        this.savedContext = context;
        if ("android.intent.action.PHONE_STATE".equals(intent.getAction())) {
            MyApplication.getInstance().loadGoogleNativeAd(context);
            try {
                String stringExtra = intent.getStringExtra("state");
                String str = outgoingSavedNumber;
                if (str == null || str.isEmpty()) {
                    outgoingSavedNumber = intent.getStringExtra("incoming_number");
                }
                if (TelephonyManager.EXTRA_STATE_IDLE.equals(stringExtra)) {
                    sendToMixpanel("EXTRA_STATE_IDLE");
                    if (!isIncomingCall && isRinging) {
                        isMissCall = true;
                        isIncomingCall = false;
                        isOutgoingCall = false;
                    }
                    String str2 = isIncomingCall ? Constants.Incoming : isMissCall ? Constants.MissCall : Constants.Outgoing;
                    if (!isShowScreen) {
                        sendToMixpanel("Call_End");
                        openNewActivity(context, outgoingSavedNumber, callStartTime1, System.currentTimeMillis(), str2);
                        isShowScreen = true;
                        return;
                    }
                    outgoingSavedNumber = null;
                } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(stringExtra)) {
                    Constants.isActivityShow = false;
                    sendToMixpanel("EXTRA_STATE_OFFHOOK");
                    if (!isRinging) {
                        isOutgoingCall = true;
                    } else {
                        callStartTime = new Date();
                        callStartTime1 = System.currentTimeMillis();
                        isIncomingCallEventSend = true;
                        sendToMixpanel("Incoming_Call");
                        isIncomingCall = true;
                    }
                    isShowScreen = false;
                } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(stringExtra)) {
                    sendToMixpanel("EXTRA_STATE_RINGING");
                    Constants.isActivityShow = false;
                    callStartTime = new Date();
                    callStartTime1 = System.currentTimeMillis();
                    isRinging = true;
                    isShowScreen = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendToMixpanel(String str) {
        if (isIncomingCallEventSend) {
        }
    }

    private void openNewActivity(Context context, String str, long date, long date2, String str2) {
        if (MyApplication.getInstance().getIsShowAfterCallDialog()) {
            if (isIncomingCallEventSend) {
                sendToMixpanel("Request_to_native_ad_load");
            }
            MyApplication.getInstance().isShowingAd = true;
            Intent intent = new Intent(context, MainCallActivity.class);
            intent.putExtra(Utils.EXTRA_MOBILE_NUMBER, str);
            intent.putExtra(Constants.StartTime, date);
            intent.putExtra(Constants.EndTime, date2);
            intent.putExtra(Constants.CallType, str2);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            outgoingSavedNumber = null;
            isMissCall = false;
            isIncomingCall = false;
            isOutgoingCall = false;
        }
    }
}
