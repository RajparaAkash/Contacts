package com.contacts.phonecontact.phonebook.dialer.DataHelper.utils;

import com.google.gson.reflect.TypeToken;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;

import java.util.List;

public class ContactConverterNumberType extends TypeToken<List<? extends PhoneNumber>> {
    ContactConverterNumberType() {
    }
}
