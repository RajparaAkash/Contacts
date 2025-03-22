package com.contacts.phonecontact.phonebook.dialer.types;

public enum EventType {
    BIRTH_DAY("Birthday"),
    ANNIVERSARY("Anniversary"),
    OTHER("Other");

    private final String eventType;

    private EventType(String str) {
        this.eventType = str;
    }

    public final String getEventType() {
        return this.eventType;
    }
}
