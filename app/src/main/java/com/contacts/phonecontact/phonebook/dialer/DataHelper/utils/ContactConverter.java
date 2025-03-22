package com.contacts.phonecontact.phonebook.dialer.DataHelper.utils;

import com.google.gson.Gson;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Address;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Email;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Event;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ContactConverter {
    private final Type addressType = new ContactConverterAddressType().getType();
    private final Type emailType = new ContactConverterEmailType().getType();
    private final Type eventType = new ContactConverterEventType().getType();
    private final Gson gson = new Gson();
    private final Type longType = new ContactConverterLongType().getType();
    private final Type numberType = new ContactConverterNumberType().getType();
    private final Type stringType = new ContactConverterStringType().getType();

    public ArrayList<Long> jsonToLongList(String str) {
        return (ArrayList) gson.fromJson(str, longType);
    }

    public String longListToJson(ArrayList<Long> arrayList) {
        return gson.toJson(arrayList);
    }

    public ArrayList<String> jsonToStringList(String str) {
        return (ArrayList) gson.fromJson(str, stringType);
    }

    public String stringListToJson(ArrayList<String> arrayList) {
        return gson.toJson(arrayList);
    }

    public ArrayList<PhoneNumber> jsonToPhoneNumberList(String str) {
        return (ArrayList) gson.fromJson(str, numberType);
    }

    public String phoneNumberListToJson(ArrayList<PhoneNumber> arrayList) {
        return gson.toJson(arrayList);
    }

    public ArrayList<Email> jsonToEmailList(String str) {
        return (ArrayList) gson.fromJson(str, emailType);
    }

    public String emailListToJson(ArrayList<Email> arrayList) {
        return gson.toJson(arrayList);
    }

    public ArrayList<Event> jsonToEventList(String str) {
        return (ArrayList) gson.fromJson(str, eventType);
    }

    public String eventListToJson(ArrayList<Event> arrayList) {
        return gson.toJson(arrayList);
    }

    public ArrayList<Address> jsonToAddressList(String str) {
        return (ArrayList) gson.fromJson(str, addressType);
    }

    public String addressListToJson(ArrayList<Address> arrayList) {
        return gson.toJson(arrayList);
    }

}
