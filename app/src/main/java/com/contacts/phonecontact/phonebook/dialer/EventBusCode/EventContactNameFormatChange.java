package com.contacts.phonecontact.phonebook.dialer.EventBusCode;

import com.contacts.phonecontact.phonebook.dialer.types.ContactNameFormatType;

import kotlin.jvm.internal.Intrinsics;

public class EventContactNameFormatChange {
    private ContactNameFormatType mContactNameFormatType;

    public EventContactNameFormatChange(ContactNameFormatType contactNameFormatType) {
        Intrinsics.checkNotNullParameter(contactNameFormatType, "mContactNameFormatType");
        this.mContactNameFormatType = contactNameFormatType;
    }

    public static EventContactNameFormatChange copy$default(EventContactNameFormatChange eventContactNameFormatChange, ContactNameFormatType contactNameFormatType, int i, Object obj) {
        if ((i & 1) != 0) {
            contactNameFormatType = eventContactNameFormatChange.mContactNameFormatType;
        }
        return eventContactNameFormatChange.copy(contactNameFormatType);
    }

    public ContactNameFormatType component1() {
        return this.mContactNameFormatType;
    }

    public EventContactNameFormatChange copy(ContactNameFormatType contactNameFormatType) {
        Intrinsics.checkNotNullParameter(contactNameFormatType, "mContactNameFormatType");
        return new EventContactNameFormatChange(contactNameFormatType);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof EventContactNameFormatChange) && this.mContactNameFormatType == ((EventContactNameFormatChange) obj).mContactNameFormatType;
    }

    public int hashCode() {
        return this.mContactNameFormatType.hashCode();
    }

    public String toString() {
        return "EventContactNameFormatChange(mContactNameFormatType=" + this.mContactNameFormatType + ')';
    }

    public ContactNameFormatType getMContactNameFormatType() {
        return this.mContactNameFormatType;
    }

}
