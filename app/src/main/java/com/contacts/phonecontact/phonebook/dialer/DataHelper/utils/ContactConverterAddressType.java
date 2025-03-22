package com.contacts.phonecontact.phonebook.dialer.DataHelper.utils;

import com.google.gson.reflect.TypeToken;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Address;

import java.util.List;

public class ContactConverterAddressType extends TypeToken<List<? extends Address>> {
    ContactConverterAddressType() {
    }
}
