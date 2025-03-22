package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import com.google.android.gms.common.internal.AccountType;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;

public class LoadAllAccountsHelper {

    public List<ContactSource> invoke(ContactDatabase contactDatabase) {
        ArrayList arrayList = new ArrayList();
        for (Object obj : CollectionsKt.sortedWith(contactDatabase.contactSourceDAO().getAllAccounts(), new Comparator<ContactSource>() {
            @Override
            public int compare(ContactSource t, ContactSource t2) {
                return ComparisonsKt.compareValues(t2.isSelected(), t.isSelected());
            }
        })) {
            ContactSource contactSource = (ContactSource) obj;
            if (Intrinsics.areEqual(contactSource.getType(), AccountType.GOOGLE) || Intrinsics.areEqual(contactSource.getType(), "Phone storage") || Intrinsics.areEqual(contactSource.getType(), "com.osp.app.signin")) {
                arrayList.add(obj);
            }
        }
        return CollectionsKt.toMutableList((Collection) arrayList);
    }

}
