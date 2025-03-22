package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.CallLogDataFetch;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;

import androidx.core.app.ActivityCompat;

import com.contacts.phonecontact.phonebook.dialer.Utils.PreferencesManager;
import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogDataModels.SIMAccount;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;

import java.util.ArrayList;
import java.util.List;

import kotlin.text.StringsKt;

public class AddAllCallLogsInDatabaseUtils {

    public static List<Contact> asList(SparseArray<Contact> sparseArray) {
        ArrayList arrayList = new ArrayList(sparseArray.size());
        int size = sparseArray.size();
        for (int i = 0; i < size; i++) {
            Contact valueAt = sparseArray.valueAt(i);
            arrayList.add(valueAt);
        }
        return arrayList;
    }

    public static TelecomManager getTelecomManager(Context context) {
        Object systemService = context.getSystemService(Context.TELECOM_SERVICE);
        return (TelecomManager) systemService;
    }


    public static ArrayList<SIMAccount> getAvailableSIMCardLabels(Context context) {
        ArrayList<SIMAccount> arrayList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
            return arrayList;
        }

        try {
            TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
            int i = 0;
            for (PhoneAccountHandle phoneAccountHandle : telecomManager.getCallCapablePhoneAccounts()) {
                PhoneAccount phoneAccount = telecomManager.getPhoneAccount(phoneAccountHandle);
                String charSequence = phoneAccount.getLabel().toString();
                String uri = phoneAccount.getAddress().toString();
                if (uri.startsWith("tel:") && !uri.substring(uri.indexOf("tel:")).isEmpty()) {
                    uri = Uri.decode(uri.substring(uri.indexOf("tel:")));
                }
                i++;
                arrayList.add(new SIMAccount(i, phoneAccount.getAccountHandle(), charSequence, uri.substring(uri.indexOf("tel:") + 4)));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

//        try {
//            if (Build.VERSION.SDK_INT >= 23) {
//                List<PhoneAccountHandle> callCapablePhoneAccounts = getTelecomManager(context).getCallCapablePhoneAccounts();
//                int i = 0;
//                for (PhoneAccountHandle t : callCapablePhoneAccounts) {
//                    int i2 = i + 1;
//                    if (i < 0) {
//                        CollectionsKt.throwIndexOverflow();
//                    }
//                    PhoneAccount phoneAccount = getTelecomManager(context).getPhoneAccount(t);
//                    String obj = phoneAccount.getLabel().toString();
//                    String uri = phoneAccount.getAddress().toString();
//                    if (StringsKt.startsWith(uri, "tel:", false)) {
//                        if (StringsKt.substringAfter(uri, "tel:", "").length() > 0) {
//                            uri = Uri.decode(StringsKt.substringAfter(uri, "tel:", ""));
//                            obj = obj + " (" + uri + ')';
//                        }
//                    }
//                    PhoneAccountHandle accountHandle = phoneAccount.getAccountHandle();
//                    arrayList.add(new SIMAccount(i2, accountHandle, obj, StringsKt.substringAfter(uri, "tel:", "")));
//                    i = i2;
//                }
//            }
//        } catch (Exception exception) {
//        }
        return arrayList;
    }
    public static ArrayList<SIMAccount> getAvailableSIMCardLabels1(Context context) {
        ArrayList<SIMAccount> simAccounts = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
            return simAccounts;
        }

//        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
//        if (subscriptionManager != null) {
//            List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
//            if (subscriptionInfoList != null) {
//                for (SubscriptionInfo info : subscriptionInfoList) {
//                    SIMAccount simAccount = new SIMAccount();
//                    // Set the subscription ID and label
//                    simAccount.setId(info.getSubscriptionId());
//                    simAccount.setLabel(info.getDisplayName().toString());
//                    // Set phone number if available
//                    simAccount.setPhoneNumber(info.getNumber() != null ? info.getNumber() : "");
//                    // Optionally, add more details if needed
//                    // For example, get telephony manager to retrieve SIM details
//                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//                    if (telephonyManager != null) {
//                        String simOperatorName = telephonyManager.getPhoneAccountHandle();
//                        simAccount.setSimOperatorName(simOperatorName);
//                    }
//                    // Add to the list
//                    simAccounts.add(simAccount);
//                }
//            } else {
//                Log.e("SIMCard", "SubscriptionInfo list is null");
//            }
//        } else {
//            Log.e("SIMCard", "SubscriptionManager is null");
//        }
//        Log.d("SIMCard", "SIM Accounts size: " + simAccounts.size());
        return simAccounts;
    }





    public static String getContactRowIDLookupList(String str, Context context) {
        String replace$default = StringsKt.replace(StringsKt.replace(str, " ", "", false), "-", "", false);
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(replace$default)), new String[]{"display_name", "_id"}, null, null, null);
        String str2 = "";
        if (query != null) {
            while (query.moveToNext()) {
//                query.getString(query.getColumnIndexOrThrow("display_name"));
                str2 = query.getString(query.getColumnIndexOrThrow("_id"));
            }
            query.close();
        }
        return str2;
    }

    public static boolean isAllPermissionGranted(Context context) {
        return PreferencesManager.hasPermission(context, new String[]{"android.permission.READ_CALL_LOG"});
    }

}
