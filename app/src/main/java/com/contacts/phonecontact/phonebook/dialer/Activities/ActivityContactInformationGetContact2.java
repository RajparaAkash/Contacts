package com.contacts.phonecontact.phonebook.dialer.Activities;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;

import kotlin.jvm.internal.MutablePropertyReference0Impl;

public class ActivityContactInformationGetContact2 extends MutablePropertyReference0Impl {
    ActivityContactInformationGetContact2(Object obj) {
        super(obj, ActivityContactInformation.class, "selectedContact", "getSelectedContact()Lcom/phone/contact/call/phonecontact/data/contact_data/Contact;", 0);
    }

    @Override
    public Object get() {
        return ((ActivityContactInformation) receiver).selectedContact;
    }

    @Override
    public void set(Object obj) {
        ((ActivityContactInformation) receiver).selectedContact = (Contact) obj;
    }
}
