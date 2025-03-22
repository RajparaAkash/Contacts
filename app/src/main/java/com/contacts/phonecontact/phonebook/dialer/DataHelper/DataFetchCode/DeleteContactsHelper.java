package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.provider.ContactsContract;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeleteContactsHelper {

    public boolean invoke(Context context, List<Contact> list) {
        try {
            ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
            Iterator<Contact> it = list.iterator();
            while (it.hasNext()) {
                ContentProviderOperation.Builder newDelete = ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI);
                newDelete.withSelection("contact_id = ?", new String[]{String.valueOf(it.next().getContactIdSimple())});
                arrayList.add(newDelete.build());
                if (arrayList.size() % 50 == 0) {
                    context.getContentResolver().applyBatch("com.android.contacts", arrayList);
                    arrayList.clear();
                }
            }
            context.getContentResolver().applyBatch("com.android.contacts", arrayList);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

}
