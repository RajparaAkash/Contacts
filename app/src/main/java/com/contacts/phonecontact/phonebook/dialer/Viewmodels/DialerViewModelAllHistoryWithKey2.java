package com.contacts.phonecontact.phonebook.dialer.Viewmodels;

import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.repo.HistoryRepo;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.repo_impl.HistoryRepoIMPL;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.LoadAllCallLogHelper;
import com.contacts.phonecontact.phonebook.dialer.types.PhoneNumberType;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class DialerViewModelAllHistoryWithKey2 implements Function0<List<? extends CallLogModel>> {
    final ContactDatabase contactDatabase;
    final List<Contact> arrayList;
    final DialerViewModel dialerViewModel;

    DialerViewModelAllHistoryWithKey2(ContactDatabase contactDatabase, DialerViewModel dialerViewModel, List<Contact> list) {
        super();
        this.contactDatabase = contactDatabase;
        this.dialerViewModel = dialerViewModel;
        this.arrayList = list;
    }

    @Override
    public List<CallLogModel> invoke() {
        final Iterable<CallLogModel> iterable = new LoadAllCallLogHelper((HistoryRepo) new HistoryRepoIMPL(contactDatabase.callLogDAO())).invoke();
        for (final CallLogModel callLogModel : iterable) {
            String name;
            if (Intrinsics.areEqual((Object) callLogModel.getName(), (Object) "")) {
                name = "Unsaved";
            } else {
                name = callLogModel.getName();
            }
            final Contact emptyContact = dialerViewModel.getEmptyContact();
            final int n = 1;
            emptyContact.setContactNumber(CollectionsKt.arrayListOf(new PhoneNumber[]{new PhoneNumber(callLogModel.getPhoneNumber(), PhoneNumberType.NO_LABEL, "", callLogModel.getPhoneNumber(), (Boolean) null, 16, (DefaultConstructorMarker) null)}));
            emptyContact.setFirstNameOriginal(name);
            emptyContact.setFirstName(name);
            if (!StringsKt.equals(callLogModel.getContactId(), "null", false) && !StringsKt.equals(callLogModel.getContactId(), "", false)) {
                final String contactId = callLogModel.getContactId();
                int int1;
                if (contactId != null) {
                    int1 = Integer.parseInt(contactId);
                } else {
                    int1 = 0;
                }
                emptyContact.setContactId(int1);
            }
            if (Intrinsics.areEqual((Object) StringsKt.trim((CharSequence) emptyContact.getFirstNameOriginal()).toString(), (Object) "")) {
                final Iterable iterable2 = emptyContact.getContactNumber();
                int n2 = 0;
                Label_0315:
                {
                    if (!(iterable2 instanceof Collection) || !((Collection) iterable2).isEmpty()) {
                        final Iterator iterator2 = iterable2.iterator();
                        while (iterator2.hasNext()) {
                            if (Intrinsics.areEqual((Object) ((PhoneNumber) iterator2.next()).getValue(), (Object) "") ^ true) {
                                n2 = n;
                                break Label_0315;
                            }
                        }
                    }
                    n2 = 0;
                }
                if (n2 != 0) {
                    emptyContact.setFirstNameOriginal(((PhoneNumber) CollectionsKt.first((List) emptyContact.getContactNumber())).getValue());
                } else {
                    emptyContact.setFirstNameOriginal("(No name)");
                }
            }
            if (!arrayList.contains(emptyContact)) {
                arrayList.add(emptyContact);
            }
        }
        return (List<CallLogModel>) iterable;
    }


}
