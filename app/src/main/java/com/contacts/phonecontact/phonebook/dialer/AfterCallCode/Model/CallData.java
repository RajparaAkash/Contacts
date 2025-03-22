package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model;

import androidx.annotation.Keep;

import java.util.ArrayList;

import kotlin.jvm.internal.DefaultConstructorMarker;

@Keep
public class CallData {
    private ArrayList<CallLog> callLogs;
    private int callLogsCount;
    private int incomingCallCount;
    private int missedCallCount;
    private int outgoingCallCount;

    public CallData() {
        this(null, 0, 0, 0, 0, 31, null);
    }

    public CallData(ArrayList<CallLog> arrayList, int i, int i2, int i3, int i4) {
        this.callLogs = arrayList;
        this.callLogsCount = i;
        this.outgoingCallCount = i2;
        this.incomingCallCount = i3;
        this.missedCallCount = i4;
    }

    public CallData(ArrayList arrayList, int i, int i2, int i3, int i4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this((i5 & 1) != 0 ? null : arrayList, (i5 & 2) != 0 ? 0 : i, (i5 & 4) != 0 ? 0 : i2, (i5 & 8) != 0 ? 0 : i3, (i5 & 16) == 0 ? i4 : 0);
    }

    public final ArrayList<CallLog> getCallLogs() {
        return this.callLogs;
    }

    public final void setCallLogs(ArrayList<CallLog> arrayList) {
        this.callLogs = arrayList;
    }

    public final int getCallLogsCount() {
        return this.callLogsCount;
    }

    public final void setCallLogsCount(int i) {
        this.callLogsCount = i;
    }

    public final int getOutgoingCallCount() {
        return this.outgoingCallCount;
    }

    public final void setOutgoingCallCount(int i) {
        this.outgoingCallCount = i;
    }

    public final int getIncomingCallCount() {
        return this.incomingCallCount;
    }

    public final void setIncomingCallCount(int i) {
        this.incomingCallCount = i;
    }

    public final int getMissedCallCount() {
        return this.missedCallCount;
    }

    public final void setMissedCallCount(int i) {
        this.missedCallCount = i;
    }
}
