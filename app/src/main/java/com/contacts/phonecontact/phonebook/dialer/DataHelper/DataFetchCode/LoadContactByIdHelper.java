package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.repo.ContactRepo;

public class LoadContactByIdHelper {
    private final ContactRepo contactRepo;
    private final String id;

    public LoadContactByIdHelper(ContactRepo contactRepo2, String str) {
        this.contactRepo = contactRepo2;
        this.id = str;
    }

    public Contact invoke() {
        return contactRepo.getContactWithId(id);
    }

}
