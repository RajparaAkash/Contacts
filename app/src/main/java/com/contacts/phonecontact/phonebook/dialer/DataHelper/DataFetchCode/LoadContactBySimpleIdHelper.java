package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.repo.ContactRepo;

import java.util.List;

public class LoadContactBySimpleIdHelper {
    private final ContactRepo contactRepo;
    private final String id;

    public LoadContactBySimpleIdHelper(ContactRepo contactRepo2, String str) {
        this.contactRepo = contactRepo2;
        this.id = str;
    }

    public List<Contact> invoke() {
        return contactRepo.getContactWithSimpleId(id);
    }

}
