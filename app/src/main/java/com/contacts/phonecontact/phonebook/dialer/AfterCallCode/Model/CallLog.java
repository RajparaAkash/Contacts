package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model;

import androidx.annotation.Keep;

import kotlin.jvm.internal.Intrinsics;

@Keep
public class CallLog {
    private String callDate;
    private long callDuration;
    private String callTime;
    private long callTimeStamp;
    private String callType;
    private String name;
    private String phoneNumber;

    public CallLog(String str, String str2, long j, long j2, String str3, String str4, String str5) {
        this.phoneNumber = str;
        this.name = str2;
        this.callDuration = j;
        this.callTimeStamp = j2;
        this.callType = str3;
        this.callDate = str4;
        this.callTime = str5;
    }


    public final String getPhoneNumber() {
        return this.phoneNumber;
    }

    public final void setPhoneNumber(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.phoneNumber = str;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.name = str;
    }

    public final long getCallDuration() {
        return this.callDuration;
    }

    public final void setCallDuration(long j) {
        this.callDuration = j;
    }

    public final long getCallTimeStamp() {
        return this.callTimeStamp;
    }

    public final void setCallTimeStamp(long j) {
        this.callTimeStamp = j;
    }

    public final String getCallType() {
        return this.callType;
    }

    public final void setCallType(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.callType = str;
    }

    public final String getCallDate() {
        return this.callDate;
    }

    public final void setCallDate(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.callDate = str;
    }

    public final String getCallTime() {
        return this.callTime;
    }

    public final void setCallTime(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.callTime = str;
    }

}
