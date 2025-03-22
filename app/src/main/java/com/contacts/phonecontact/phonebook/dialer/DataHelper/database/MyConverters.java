package com.contacts.phonecontact.phonebook.dialer.DataHelper.database;

import androidx.room.TypeConverter;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Address;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Email;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Event;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyConverters {

    @TypeConverter
    public static ArrayList<Long> getContactGroup(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<ArrayList<Long>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String setContactGroup(ArrayList<Long> myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }


    @TypeConverter
    public static ArrayList<Address> getContactAddresses(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<ArrayList<Address>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String setContactAddresses(ArrayList<Address> myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }


    @TypeConverter
    public static ArrayList<Email> getContactEmail(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<ArrayList<Email>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String setContactEmail(ArrayList<Email> myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }


    @TypeConverter
    public static ArrayList<Event> getContactEvent(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<ArrayList<Event>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String setContactEvent(ArrayList<Event> myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }


    @TypeConverter
    public static ArrayList<String> getContactNotes(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String setContactNotes(ArrayList<String> myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }


    @TypeConverter
    public static ArrayList<PhoneNumber> getContactNumber(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<ArrayList<PhoneNumber>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String setContactNumber(ArrayList<PhoneNumber> myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }

}
