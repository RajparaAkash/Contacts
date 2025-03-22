package com.contacts.phonecontact.phonebook.dialer.DataHelper.utils;

import com.google.gson.reflect.TypeToken;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Event;

import java.util.List;

public class ContactConverterEventType extends TypeToken<List<? extends Event>> {
    ContactConverterEventType() {
    }
}
