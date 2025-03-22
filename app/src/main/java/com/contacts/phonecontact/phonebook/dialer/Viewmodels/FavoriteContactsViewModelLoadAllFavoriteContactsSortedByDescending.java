package com.contacts.phonecontact.phonebook.dialer.Viewmodels;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;

import java.util.Comparator;

import kotlin.comparisons.ComparisonsKt;

public class FavoriteContactsViewModelLoadAllFavoriteContactsSortedByDescending implements Comparator<Contact> {
    @Override
    public int compare(Contact t, Contact t2) {
        return ComparisonsKt.compareValues(t2.getStringToCompare().length(), t.getStringToCompare().length());
    }
}
