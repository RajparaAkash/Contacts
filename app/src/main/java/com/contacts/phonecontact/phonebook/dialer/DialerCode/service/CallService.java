package com.contacts.phonecontact.phonebook.dialer.DialerCode.service;

import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.net.Uri;
import android.telecom.Call;
import android.telecom.InCallService;

import com.contacts.phonecontact.phonebook.dialer.Activities.CallActivity;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.CallNotificationManager;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.CallManager;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.NoCall;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.Utils.CallUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class CallService extends InCallService {

    private final CallServiceCallListener1 callListener = new CallServiceCallListener1(this);
    public CallNotificationManager callNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        this.callNotificationManager = new CallNotificationManager(this);
    }

    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        CallManager.Companion.onCallAdded(call);
        CallManager.Companion.setInCallService(this);
        call.registerCallback(callListener);
        Object systemService = getSystemService(Context.KEYGUARD_SERVICE);
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.app.KeyguardManager");
        boolean isDeviceLocked = ((KeyguardManager) systemService).isDeviceLocked();
        if (call.getDetails() != null && call.getDetails().getHandle() != null) {
            Uri handle = call.getDetails().getHandle();
            Intrinsics.checkNotNull(handle);
            String decode = Uri.decode(handle.toString());
            String str = decode;
            boolean z = false;
            if (!(str == null || str.length() == 0)) {
                if (StringsKt.startsWith(decode, "tel:", false)) {
                    String replace$default = StringsKt.replace(decode.toString(), "tel:", "", false);
                    CallService callService = this;
//                    if (ContaxtExtUtils.isDefault(callService)) {
//                        List<BlockContact> list = new GetBlockContactUseCase(callService).invoke();
//                        boolean z2 = false;
//                        for (BlockContact blockContact : list) {
//                            String str2 = replace$default;
//                            if (StringsKt.equals(PhoneNumberUtils.formatNumber(blockContact.getValue().replace("+91", "").trim(), "IN"), str.replace("+91", "").trim(), false) || StringsKt.contains((CharSequence) str, (CharSequence) blockContact.getNormalizedNumber(), false)) {
////                            if (StringsKt.contains((CharSequence) blockContact.getNormalizedNumber(), (CharSequence) str2, false) || StringsKt.contains((CharSequence) str2, (CharSequence) blockContact.getNormalizedNumber(), false)) {
//                                z2 = true;
//                            }
//                        }
//                        z = z2;
//                    }
                }
            }
//            if (z) {
//                return;
//            }
            if (!ContaxtExtUtils.getPowerManager(this).isInteractive() || CallUtils.isOutgoing(call) || isDeviceLocked) {
                try {
                    MyApplication.getInstance().isShowingAd = true;
                    startActivity(CallActivity.Companion.getStartIntent(this));
                    if (CallUtils.getStateCompat(call) != 2) {
                        callNotificationManager.setupNotification();
                    }
                } catch (ActivityNotFoundException exception) {
                    callNotificationManager.setupNotification();
                }
            } else {
                callNotificationManager.setupNotification();
            }
        }
    }

    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        call.unregisterCallback(callListener);
        boolean areEqual = Intrinsics.areEqual(call, CallManager.Companion.getPrimaryCall());
        CallManager.Companion.onCallRemoved(call);
        if (Intrinsics.areEqual(CallManager.Companion.getPhoneState(), NoCall.INSTANCE)) {

            CallManager.Companion.setInCallService(null);
            callNotificationManager.cancelNotification();
            return;
        }
        callNotificationManager.setupNotification();
        if (areEqual) {
            MyApplication.getInstance().isShowingAd = true;
            startActivity(CallActivity.Companion.getStartIntent(this));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callNotificationManager.cancelNotification();
    }
}
