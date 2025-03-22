package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.CallLogData;

import android.content.Context;
import android.database.Cursor;
import android.os.CancellationSignal;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.ConstantsKt;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.PreferencesManager;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.Utils;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.CallData;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.Country;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class CallLogFetcher {
    private final Context context;
    private int callLogCount;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy MMM dd");
    private SimpleDateFormat formatter1 = new SimpleDateFormat(ConstantsKt.TIME_FORMAT_12);
    private int incomingCallCount;
    private int missedCallCount;
    private SimpleDateFormat monthFormatter = new SimpleDateFormat("MMM");
    private int outgoingCallCount;

    public CallLogFetcher(Context context2) {
        this.context = context2;
    }

    public CallData getCallLogs(String str) {
        final ArrayList list = new ArrayList();
        final String replace$default = StringsKt.replace(StringsKt.replace(str, " ", "", false), "-", "", false);
        if (context != null && ActivityCompat.checkSelfPermission(context, "android.permission.READ_CALL_LOG") == 0) {
            final CharSequence charSequence = replace$default;
            int n = charSequence.length() - 1;
            int i = 0;
            int n2 = 0;
            while (i <= n) {
                int n3;
                if (n2 == 0) {
                    n3 = i;
                } else {
                    n3 = n;
                }
                final boolean b = Intrinsics.compare((int) charSequence.charAt(n3), 32) <= 0;
                if (n2 == 0) {
                    if (!b) {
                        n2 = 1;
                    } else {
                        ++i;
                    }
                } else {
                    if (!b) {
                        break;
                    }
                    --n;
                }
            }
            Cursor cursor;
            if (StringsKt.startsWith(charSequence.subSequence(i, n + 1).toString(), "+", false)) {
                str = phoneNumberWithOutCountryCode(context, replace$default);
                Intrinsics.checkNotNull((Object) str);
                cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, (String[]) null, "number = ? OR number = ?", new String[]{replace$default, str}, "date DESC", (CancellationSignal) null);
                Intrinsics.checkNotNull((Object) cursor);
            } else {
                str = getLocaleCountryDialCode(context);
                Intrinsics.checkNotNull((Object) str);
                final StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(replace$default);
                str = sb.toString();
                cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, (String[]) null, "number = ? OR number = ?", new String[]{replace$default, str}, "date DESC", (CancellationSignal) null);
                Intrinsics.checkNotNull((Object) cursor);
            }
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    final int int1 = cursor.getInt(cursor.getColumnIndexOrThrow("type"));
                    final long long1 = cursor.getLong(cursor.getColumnIndexOrThrow("date"));
                    final long long2 = cursor.getLong(cursor.getColumnIndexOrThrow("duration"));
                    final String string = cursor.getString(cursor.getColumnIndexOrThrow("number"));
                    Log.e("number------->", string);
                    final Date date = new Date(long1);
                    final String format = monthFormatter.format(date);
                    final String format2 = monthFormatter.format(new Date(System.currentTimeMillis()));
                    final String format3 = formatter1.format(date);
                    if (Intrinsics.areEqual((Object) format, (Object) format2)) {
                        ++callLogCount;
                    }
                    final String format4 = formatter.format(date);
                    final String format5 = formatter.format(System.currentTimeMillis());
                    final String format6 = formatter.format(PreferencesManager.Companion.getInstance(context).getAppUpdateDate());
                    final String format7 = formatter1.format(System.currentTimeMillis());
                    final String format8 = formatter1.format(PreferencesManager.Companion.getInstance(context).getAppUpdateDate());
                    Label_0783:
                    {
                        if (Intrinsics.areEqual((Object) format4, (Object) format5) && Intrinsics.areEqual((Object) format5, (Object) format6)) {
                            if (format7.compareTo(format8) >= 0) {
                                if (int1 == 1) {
                                    ++incomingCallCount;
                                    break Label_0783;
                                }
                                if (int1 == 2) {
                                    ++outgoingCallCount;
                                    break Label_0783;
                                }
                                if (int1 != 3) {
                                    break Label_0783;
                                }
                                ++missedCallCount;
                                break Label_0783;
                            }
                        }
                        if (Intrinsics.areEqual((Object) format4, (Object) format5)) {
                            if (int1 != 1) {
                                if (int1 != 2) {
                                    if (int1 == 3) {
                                        ++missedCallCount;
                                    }
                                } else {
                                    ++outgoingCallCount;
                                }
                            } else {
                                ++incomingCallCount;
                            }
                        }
                    }
                    if (Intrinsics.areEqual((Object) format, (Object) format2) && int1 == 3 && list.size() < 1) {
                        final String string2 = context.getString(R.string.title_today);
                        final String string3 = context.getString(R.string.missed_Call);
                        list.add(new com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.CallLog(replace$default, "name", long2, long1, string3, string2, format3));
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return new CallData(list, callLogCount, outgoingCallCount, incomingCallCount, missedCallCount);
    }

    private String phoneNumberWithOutCountryCode(Context context2, String str) {
        try {
            Phonenumber.PhoneNumber parse = PhoneNumberUtil.createInstance(context2).parse(str, "");
            return String.valueOf(parse.getNationalNumber());
        } catch (NumberParseException e) {
            PrintStream printStream = System.err;
            printStream.println("NumberParseException was thrown: " + e);
            return str;
        }
    }

    private String getLocaleCountryDialCode(Context context2) {
        if (context2 == null) {
            return "+91";
        }
        Object systemService = context2.getSystemService(Context.TELEPHONY_SERVICE);
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

}
