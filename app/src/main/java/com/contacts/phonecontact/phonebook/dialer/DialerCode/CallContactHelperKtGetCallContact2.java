package com.contacts.phonecontact.phonebook.dialer.DialerCode;

import android.content.Context;
import android.net.Uri;
import android.telecom.Call;
import android.util.Log;

import com.contacts.phonecontact.phonebook.dialer.AllModels.CallContact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.dao.ContactDAO;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.repo.ContactRepo;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.repo_impl.ContactRepoIMPL;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting.ContactOrder;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting.FilterType;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting.SortingType;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.LoadAllContactHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class CallContactHelperKtGetCallContact2 implements Function0<Unit> {
    final Call call;
    final CallContact callContact;
    final Function1<CallContact, Unit> function1;
    final Context context;
    final ContactDatabase contactDatabase;

    CallContactHelperKtGetCallContact2(Call call, Function1<? super CallContact, Unit> function1, CallContact callContact, ContactDatabase contactDatabase, Context context) {
        super();
        this.call = call;
        this.function1 = (Function1<CallContact, Unit>) function1;
        this.callContact = callContact;
        this.contactDatabase = contactDatabase;
        this.context = context;
    }

    @Override
    public Unit invoke() {
        String string = null;
        Label_0038:
        {
            try {
                if (call != null) {
                    final Call.Details details = call.getDetails();
                    if (details != null) {
                        final Uri handle = details.getHandle();
                        if (handle != null) {
                            string = handle.toString();
                            break Label_0038;
                        }
                    }
                }
            } catch (final NullPointerException ex) {
            }
            string = null;
        }
        if (string == null) {
            function1.invoke(callContact);
            return Unit.INSTANCE;
        }
        final String decode = Uri.decode(string);
        if (StringsKt.startsWith(decode, "tel:", false)) {
            final String substringAfter$default = StringsKt.substringAfter(decode, "tel:", "");
            ContactDAO contactDAO;
            if (contactDatabase != null) {
                contactDAO = contactDatabase.contactDAO();
            } else {
                contactDAO = null;
            }
            final List invoke = new LoadAllContactHelper(context, (ContactRepo) new ContactRepoIMPL(contactDAO), (ContactOrder) new ContactOrder.Title((SortingType) SortingType.Ascending.INSTANCE), FilterType.ALL).invoke();
            final StringBuilder sb = new StringBuilder();
            sb.append("load contact 34: contact list size --> ");
            sb.append(invoke.size());
            System.out.println((Object) sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("load contact : contact list size it--> ");
            sb2.append(invoke.size());
            System.out.println((Object) sb2.toString());
            final Iterable<Contact> iterable = invoke;
            Label_0256:
            for (Contact contact : iterable) {
                callContact.setNumber(substringAfter$default);
                final Iterable<PhoneNumber> iterable2 = contact.getContactNumber();
                final Collection collection = new ArrayList();
                for (final Object next : iterable2) {
                    final PhoneNumber phoneNumber = (PhoneNumber) next;
                    Log.e("value ------>", phoneNumber.getValue());
                    if (Intrinsics.areEqual((Object) phoneNumber.getValue(), (Object) substringAfter$default)) {
                        collection.add(next);
                    }
                }
                final List list = (List) collection;
                final boolean empty = list.isEmpty();
                String photoUri = "";
                if (!empty) {
                    callContact.setName(contact.getFirstNameOriginal());
                    final String contactPhotoUri = contact.getContactPhotoUri();
                    if (contactPhotoUri != null) {
                        photoUri = contactPhotoUri;
                    }
                    callContact.setPhotoUri(photoUri);
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("contact details : ");
                    sb3.append(contact.getFirstNameOriginal());
                    System.out.println((Object) sb3.toString());
                    if (list.size() > 1) {
                        while (true) {
                            for (final Object next2 : list) {
                                if (Intrinsics.areEqual((Object) ((PhoneNumber) next2).getValue(), (Object) substringAfter$default)) {
                                    if (next2 != null) {
                                        callContact.setNumberLabel(((PhoneNumber) contact.getContactNumber().get(0)).getLabel());
                                        continue Label_0256;
                                    }
                                    continue Label_0256;
                                }
                            }
                            Object next2 = null;
                            continue;
                        }
                    }
                    continue;
                } else {
                    if (!Intrinsics.areEqual((Object) callContact.getName(), (Object) "")) {
                        continue;
                    }
                    callContact.setName(substringAfter$default);
                }
            }
        }
        return Unit.INSTANCE;
    }

}
