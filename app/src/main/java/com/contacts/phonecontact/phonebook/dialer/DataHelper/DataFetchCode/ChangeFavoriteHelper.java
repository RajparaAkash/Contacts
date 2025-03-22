package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kotlin.collections.CollectionsKt;

public class ChangeFavoriteHelper {

    public void invoke(Context context, List<Contact> list, int i) {
        try {
            ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
            List<Contact> list2 = list;
            ArrayList<String> arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
            Iterator<Contact> it = list2.iterator();
            while (it.hasNext()) {
                arrayList2.add(String.valueOf(it.next().getContactIdSimple()));
            }
            for (String str : arrayList2) {
                ContentProviderOperation.Builder newUpdate = ContentProviderOperation.newUpdate(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, str));
                newUpdate.withValue("starred", i);
                arrayList.add(newUpdate.build());
            }
            context.getContentResolver().applyBatch("com.android.contacts", arrayList);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
