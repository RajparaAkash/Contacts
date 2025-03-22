package com.contacts.phonecontact.phonebook.dialer.AllModels;

import androidx.annotation.Keep;

import kotlin.jvm.internal.Intrinsics;

@Keep
public class DataAllCallLog extends ObjectCallLog {
    private final String allCallLog;

    public DataAllCallLog(String str) {
        this.allCallLog = str;
    }


    public DataAllCallLog copy(String str) {
        return new DataAllCallLog(str);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof DataAllCallLog) && Intrinsics.areEqual(this.allCallLog, ((DataAllCallLog) obj).allCallLog);
    }

    @Override
    public int getType() {
        return 2;
    }

    public int hashCode() {
        return this.allCallLog.hashCode();
    }

    public String toString() {
        return "DataAllCallLog(allCallLog=" + this.allCallLog + ')';
    }

    public String getAllCallLog() {
        return this.allCallLog;
    }

}
