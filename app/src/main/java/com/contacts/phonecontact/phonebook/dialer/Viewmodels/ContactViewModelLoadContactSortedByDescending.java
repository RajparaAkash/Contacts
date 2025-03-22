package com.contacts.phonecontact.phonebook.dialer.Viewmodels;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;

import java.util.Comparator;

import kotlin.comparisons.ComparisonsKt;

public class ContactViewModelLoadContactSortedByDescending implements Comparator<Contact> {
    @Override
    public int compare(Contact t, Contact t2) {
        return ComparisonsKt.compareValues(t2.getContactNumber().size(), t.getContactNumber().size());
    }
}
