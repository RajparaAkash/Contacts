package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.dao.ContactSourcesDAO;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.CursorExtenUtils;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.PreferencesManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadAllAccountsNameHelper2 {

    private int counter;
    ArrayList<ContactSource> arrayList;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor(); // Use ExecutorService for background tasks
    public ArrayList<ContactSource> invoke(Context context, ContactSourcesDAO contactSourcesDAO) {
        arrayList = new ArrayList<>();
        executorService.execute(() -> {
            int[] intArray = context.getResources().getIntArray(R.array.thumb_color);
            if (isAllPermissionGranted(context)) {
                Account[] accounts = AccountManager.get(context).getAccounts();
                arrayList = new ArrayList<>(getContentResolverAccounts(context));
                for (Account account : accounts) {
                    if (ContentResolver.getIsSyncable(account, "com.android.contacts") == 1) {
                        String accountName = account.name;
                        int color = intArray[counter % intArray.length];
                        counter++;
                        ContactSource accountWithName = contactSourcesDAO.getAccountWithName(accountName);
                        if (accountWithName == null) {
                            accountWithName = new ContactSource(accountName, accountName, account.type, color, false, 16, null);
                        }
                        arrayList.add(accountWithName);
                    }
                }
                ContactSource phoneStorageSource = contactSourcesDAO.getAccountWithName("Phone storage");
                if (phoneStorageSource == null) {
                    phoneStorageSource = new ContactSource("Phone storage", "Phone storage", "Phone storage", ContextCompat.getColor(context, R.color.gray), true);
                }
                arrayList.add(phoneStorageSource);
                contactSourcesDAO.addAllAccounts(arrayList);
//                Log.e("fatal4", "LoadAllAccountsNameHelper2: " + arrayList.size() );
            }
        });
        return arrayList;
    }
    private ArrayList<ContactSource> getContentResolverAccounts(Context context) {
        ArrayList<ContactSource> arrayList = new ArrayList<>();
        Uri[] uriArr = {ContactsContract.Groups.CONTENT_URI, ContactsContract.Settings.CONTENT_URI, ContactsContract.RawContacts.CONTENT_URI};
        for (Uri uri : uriArr) {
            fillSourcesFromUri(context, uri, arrayList);
        }
        return arrayList;
    }
    private void fillSourcesFromUri(Context context, Uri uri, ArrayList<ContactSource> arrayList) {
        String[] projection = {"account_name", "account_type"};
        int[] intArray = context.getResources().getIntArray(R.array.thumb_color);
        if (isAllPermissionGranted(context)) {
            try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        String accountName = CursorExtenUtils.getStringValue(cursor, "account_name");
                        String accountType = CursorExtenUtils.getStringValue(cursor, "account_type");
                        int color = intArray[counter % intArray.length];
                        counter++;
                        arrayList.add(new ContactSource(accountName != null ? accountName : "",
                                accountName != null ? accountName : "",
                                accountType != null ? accountType : "",
                                color,
                                false,
                                16,
                                null));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private boolean isAllPermissionGranted(Context context) {
        return PreferencesManager.hasPermission(context, new String[]{
                "android.permission.READ_CONTACTS",
                "android.permission.WRITE_CONTACTS"
        });
    }
    // Callback interface to handle asynchronous results
    public interface Callback {
        void onComplete(ArrayList<ContactSource> result);
    }

}
