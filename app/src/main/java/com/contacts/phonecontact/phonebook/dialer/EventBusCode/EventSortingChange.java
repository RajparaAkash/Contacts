package com.contacts.phonecontact.phonebook.dialer.EventBusCode;

import com.contacts.phonecontact.phonebook.dialer.types.ContactSorting;

public class EventSortingChange {
    private ContactSorting sortingType;

    public EventSortingChange(ContactSorting contactSorting) {
        this.sortingType = contactSorting;
    }

    public static EventSortingChange copy$default(EventSortingChange eventSortingChange, ContactSorting contactSorting, int i, Object obj) {
        if ((i & 1) != 0) {
            contactSorting = eventSortingChange.sortingType;
        }
        return eventSortingChange.copy(contactSorting);
    }

    public ContactSorting component1() {
        return this.sortingType;
    }

    public EventSortingChange copy(ContactSorting contactSorting) {
        return new EventSortingChange(contactSorting);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof EventSortingChange) && this.sortingType == ((EventSortingChange) obj).sortingType;
    }

    public int hashCode() {
        return this.sortingType.hashCode();
    }

    public String toString() {
        return "EventSortingChange(sortingType=" + this.sortingType + ')';
    }

    public ContactSorting getSortingType() {
        return this.sortingType;
    }

}
