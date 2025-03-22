package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import kotlin.ranges.RangesKt;

public class AddAllContactsInDatabaseHelper {
    private final ContactDatabase mContactDAO;
    private final Context mContext;

    public AddAllContactsInDatabaseHelper(Context context, ContactDatabase contactDatabase) {
        this.mContext = context;
        this.mContactDAO = contactDatabase;
    }

    public void invoke() {
        SparseArray<Contact> invoke = new GetAllContactNumberHelper(mContext, mContactDAO).invoke();
        ArrayList<Contact> arrayList = new ArrayList(invoke.size());
        ArrayList<Number> arrayList2 = new ArrayList();
        for (Number number : RangesKt.until(0, invoke.size())) {
//            ((Number) number).intValue();
            arrayList2.add(number);
        }
        for (Number number : arrayList2) {
            arrayList.add(invoke.valueAt(number.intValue()));
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Contact obj2 : arrayList) {
            Integer valueOf = ((Contact) obj2).getContactIdSimple();
            Object obj3 = linkedHashMap.get(valueOf);
            if (obj3 == null) {
                obj3 = (List) new ArrayList();
                linkedHashMap.put(valueOf, obj3);
            }
            ((List) obj3).add(obj2);
        }
        Log.e("safwsegedyh", String.valueOf(arrayList.size()));
        mContactDAO.contactDAO().deleteAllContact();
        mContactDAO.contactDAO().addAllContacts(arrayList);
    }

}
