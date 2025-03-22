package com.contacts.phonecontact.phonebook.dialer.DialerCode.service;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telecom.Call;
import android.telecom.CallScreeningService;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.contacts.phonecontact.phonebook.dialer.DialerCode.CallerIdPopup;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.Utils;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.Contact;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.Country;
import com.contacts.phonecontact.phonebook.dialer.Utils.AppUtils;
import com.contacts.phonecontact.phonebook.dialer.AllModels.BlockContact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.GetBlockContactHelper;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.CryptLib;

import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import javax.crypto.NoSuchPaddingException;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class SimpleCallScreeningService extends CallScreeningService {

    public static final Companion Companion = new Companion(null);
    public static CallerIdPopup callerIdPopup;
    private CryptLib cryptLib;

    public void onCreate() {
        super.onCreate();
        try {
            this.cryptLib = new CryptLib();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public void onScreenCall(Call.Details details) {
        String uri = details.getHandle() != null ? details.getHandle().toString() : "";
        CallResponse.Builder builder = new CallResponse.Builder();
        SimpleCallScreeningService simpleCallScreeningService = this;
        boolean z = false;
        if (ContaxtExtUtils.isDefault(simpleCallScreeningService)) {
            List<BlockContact> list = new GetBlockContactHelper(simpleCallScreeningService).invoke();
            boolean z2 = false;
            for (BlockContact blockContact : list) {
                String str = uri;
                if (StringsKt.equals(PhoneNumberUtils.formatNumber(blockContact.getValue().replace("+91", "").trim(), "IN"), str.replace("+91", "").trim(), false) || StringsKt.contains((CharSequence) str, (CharSequence) blockContact.getNormalizedNumber(), false)) {
//                if (StringsKt.contains((CharSequence) blockContact.getNormalizedNumber(), (CharSequence) str, false) || StringsKt.contains((CharSequence) str, (CharSequence) blockContact.getNormalizedNumber(), false)) {
                    builder.setDisallowCall(true);
                    builder.setRejectCall(true);
                    builder.setSkipCallLog(false);
                    z2 = true;
                }
            }
            z = z2;
        }
        if (!z) {
            if (AppUtils.isNetworkAvailable(simpleCallScreeningService)) {
                searchNumber(uri);
            } else {
                startCallerScreen(uri, "", "0");
            }
            respondToCall(details, builder.build());
        }
    }

    private void searchNumber(String str) {
        Contact contact = Utils.getContact(this, str);
        if (contact != null) {
            startCallerScreen(str, contact.getNameSuffix(), "0");
        } else {
            startCallerScreen(str, "", "0");
        }
    }

    private void startCallerScreen(String str, String str2, String str3) {
        try {
            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= 26) {
                        if (Settings.canDrawOverlays(SimpleCallScreeningService.this)) {
                            Companion.setCallerIdPopup(new CallerIdPopup(SimpleCallScreeningService.this, StringsKt.replace(StringsKt.replace(str, "tel:", "", false), "%2B", "+", false), str2, str3));
                        }
                    }
                }
            }, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String phoneNumberWithOutCountryCode(Context context, String str) {
        try {
            Phonenumber.PhoneNumber parse = PhoneNumberUtil.createInstance(context).parse(str, "");
            return String.valueOf(parse.getNationalNumber());
        } catch (NumberParseException e) {
            PrintStream printStream = System.err;
            printStream.println("NumberParseException was thrown: " + e);
            return str;
        }
    }

    private String getLocaleCountryDialCode(Context context) {
        if (context == null) {
            return "+91";
        }
        Object systemService = context.getSystemService(Context.TELEPHONY_SERVICE);
        String simCountryIso = ((TelephonyManager) systemService).getSimCountryIso();
        Country[] countryArr = Utils.COUNTRIES;
        for (Country country : countryArr) {
            String code = country.getCode();
            String upperCase = simCountryIso.toUpperCase(Locale.ROOT);
            if (code.equals(upperCase)) {
                return country.getDialCode();
            }
        }
        return "+91";
    }

    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
        Log.e("onTaskRemoved-------------->", "onTaskRemoved");
    }

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public CallerIdPopup getCallerIdPopup() {
            CallerIdPopup callerIdPopup = SimpleCallScreeningService.callerIdPopup;
            if (callerIdPopup != null) {
                return callerIdPopup;
            }
            Intrinsics.throwUninitializedPropertyAccessException("callerIdPopup");
            return null;
        }

        public void setCallerIdPopup(CallerIdPopup callerIdPopup) {
            SimpleCallScreeningService.callerIdPopup = callerIdPopup;
        }
    }
}
