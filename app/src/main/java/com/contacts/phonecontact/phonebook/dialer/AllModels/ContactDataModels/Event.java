package com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels;

import androidx.annotation.Keep;

import com.contacts.phonecontact.phonebook.dialer.types.EventType;

import java.io.Serializable;

import kotlin.jvm.internal.Intrinsics;

@Keep
public class Event implements Serializable {
    private EventType type;
    private String value;

    public Event(String str, EventType eventType) {
        this.value = str;
        this.type = eventType;
    }


    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Event)) {
            return false;
        }
        Event event = (Event) obj;
        return Intrinsics.areEqual(this.value, event.value) && this.type == event.type;
    }

    public int hashCode() {
        return (this.value.hashCode() * 31) + this.type.hashCode();
    }

    public String toString() {
        return "Event(value=" + this.value + ", type=" + this.type + ')';
    }

    public EventType getType() {
        return this.type;
    }

    public void setType(EventType eventType) {
        this.type = eventType;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

}
