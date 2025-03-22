package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;

import androidx.core.content.ContextCompat;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.dao.ContactSourcesDAO;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.CursorExtenUtils;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.PreferencesManager;

import java.util.ArrayList;

public class LoadAllAccountsNameHelper {
    private int counter;

    public ArrayList<ContactSource> invoke(Context context, ContactSourcesDAO contactSourcesDAO, OnBatchCompleteListener listener) {
        int[] intArray = context.getResources().getIntArray(R.array.thumb_color);
        ArrayList<ContactSource> arrayList = new ArrayList<>();
        if (isAllPermissionGranted(context)) {
            Account[] accounts = AccountManager.get(context).getAccounts();
            arrayList.addAll(getContentResolverAccounts(context));
            for (Account account : accounts) {
                if (ContentResolver.getIsSyncable(account, "com.android.contacts") == 1) {
                    String str = account.name;
                    int i = this.counter + 1;
                    this.counter = i;
                    if (i >= 5) {
                        this.counter = 0;
                    }
                    ContactSource accountWithName = contactSourcesDAO.getAccountWithName(str);
                    if (accountWithName == null) {
                        String str2 = account.name;
                        String str3 = account.type;
                        int i2 = intArray[this.counter];
                        accountWithName = new ContactSource(str, str2, str3, Integer.valueOf(i2), false, 16, null);
                    }
                    arrayList.add(accountWithName);
                }
            }
            ContactSource accountWithName2 = contactSourcesDAO.getAccountWithName("Phone storage");
            if (accountWithName2 == null) {
                accountWithName2 = new ContactSource("Phone storage", "Phone storage", "Phone storage", Integer.valueOf(ContextCompat.getColor(context, R.color.gray)), true);
            }
            arrayList.add(accountWithName2);
            contactSourcesDAO.addAllAccounts(arrayList);
            listener.onBatchComplete();
        }
        return arrayList;
    }

    private ArrayList<ContactSource> getContentResolverAccounts(Context context) {
        ArrayList<ContactSource> arrayList = new ArrayList<>();
        Uri[] uriArr = {ContactsContract.Groups.CONTENT_URI, ContactsContract.Settings.CONTENT_URI, ContactsContract.RawContacts.CONTENT_URI};
        for (int i = 0; i < 3; i++) {
            Uri uri = uriArr[i];
            fillSourcesFromUri(context, uri, arrayList);
        }
        return arrayList;
    }

    private void fillSourcesFromUri(Context context, Uri uri, ArrayList<ContactSource> arrayList) {
        String str;
        String[] strArr = {"account_name", "account_type"};
        int[] intArray = context.getResources().getIntArray(R.array.thumb_color);
        if (isAllPermissionGranted(context)) {
            try {
                Cursor cursor = context.getContentResolver().query(uri, strArr, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            String stringValue = CursorExtenUtils.getStringValue(cursor, "account_name");
                            String str2 = stringValue == null ? "" : stringValue;
                            String stringValue2 = CursorExtenUtils.getStringValue(cursor, "account_type");
                            if (stringValue2 == null) {
                                str = "";
                            } else {
                                str = stringValue2;
                            }
                            int i = this.counter + 1;
                            this.counter = i;
                            if (i >= 5) {
                                this.counter = 0;
                            }
                            arrayList.add(new ContactSource(str2, str2, str, intArray[counter], false, 16, null));
                        } while (cursor.moveToNext());
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isAllPermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return PreferencesManager.hasPermission(context, new String[]{"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS"});
        }
        return true;
    }

    public interface OnBatchCompleteListener {
        void onBatchComplete();
    }
}
