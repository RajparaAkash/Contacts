package com.contacts.phonecontact.phonebook.dialer.AllModels;

import androidx.annotation.Keep;

import kotlin.jvm.internal.Intrinsics;

@Keep
public class DataCallLogHeader extends ObjectCallLog {
    private final String header;

    public DataCallLogHeader(String str) {
        this.header = str;
    }


    public DataCallLogHeader copy(String str) {
        return new DataCallLogHeader(str);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof DataCallLogHeader) && Intrinsics.areEqual(this.header, ((DataCallLogHeader) obj).header);
    }

    @Override
    public int getType() {
        return 0;
    }

    public int hashCode() {
        return this.header.hashCode();
    }

    public String toString() {
        return "DataCallLogHeader(header=" + this.header + ')';
    }

    public String getHeader() {
        return this.header;
    }

}
