package com.contacts.phonecontact.phonebook.dialer.DataHelper.repo;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;

import java.util.List;

public interface ContactRepo {
    void addAllContacts(List<Contact> list);

    long addContact(Contact contact);

    void deleteAllContact();

    void deleteContact(Contact contact);

    List<Contact> getAllContacts();

    Contact getContactWithId(String str);

    List<Contact> getContactWithSimpleId(String str);

    void refreshAccounts(Contact contact);
}
