package com.contacts.phonecontact.phonebook.dialer.interfaces;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;

import java.util.List;

public interface OnContactDataUpdate {
    void onUpdate(List<Contact> list);
}
