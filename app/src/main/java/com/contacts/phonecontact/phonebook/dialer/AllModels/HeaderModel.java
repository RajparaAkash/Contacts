package com.contacts.phonecontact.phonebook.dialer.AllModels;

import androidx.annotation.Keep;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;

import kotlin.jvm.internal.Intrinsics;

@Keep
public class HeaderModel extends ListObject {
    private final String contactHeading;
    private final Contact mContact;

    public HeaderModel(String str, Contact contact) {
        this.contactHeading = str;
        this.mContact = contact;
    }


    public HeaderModel copy(String str, Contact contact) {
        return new HeaderModel(str, contact);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HeaderModel)) {
            return false;
        }
        HeaderModel headerModel = (HeaderModel) obj;
        return Intrinsics.areEqual(this.contactHeading, headerModel.contactHeading) && Intrinsics.areEqual(this.mContact, headerModel.mContact);
    }

    @Override
    public int getType() {
        return 0;
    }

    public int hashCode() {
        return (this.contactHeading.hashCode() * 31) + this.mContact.hashCode();
    }

    public String toString() {
        return "HeaderModel(contactHeading=" + this.contactHeading + ", mContact=" + this.mContact + ')';
    }

    public String getContactHeading() {
        return this.contactHeading;
    }

    public Contact getMContact() {
        return this.mContact;
    }

}
