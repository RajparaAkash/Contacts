package com.contacts.phonecontact.phonebook.dialer.AllModels;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.CallLog$$ExternalSyntheticBackport0;

import java.io.Serializable;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Keep
@Entity(tableName = "CallLogModel")
public class CallLogModel extends ObjectCallLog implements Serializable {
    @PrimaryKey
    private Integer dummyId;
    private int id;
    private String contactId;
    private String phoneNumber;
    private String name;
    private String photoUri;
    private int startTS;
    private int duration;
    private int callType;
    private int simID;
    private String specificNumber;
    private String specificType;
    private String callLogType;
    private long callLogTime;
    private Integer bgColor;

    public CallLogModel() {

    }

    public CallLogModel(Integer num, int i, String str, String str2, String str3, String str4, int i2, int i3, int i4, int i5, String str5, String str6, String str7, long j, Integer num2, int i6, DefaultConstructorMarker defaultConstructorMarker) {
        this((i6 & 1) != 0 ? null : num, i, (i6 & 4) != 0 ? null : str, str2, str3, str4, i2, i3, i4, i5, str5, str6, str7, j, (i6 & 16384) != 0 ? -16776961 : num2);
    }

    public CallLogModel(Integer num, int i, String str, String str2, String str3, String str4, int i2, int i3, int i4, int i5, String str5, String str6, String str7, long j, Integer num2) {
        this.dummyId = num;
        this.id = i;
        this.contactId = str;
        this.phoneNumber = str2;
        this.name = str3;
        this.photoUri = str4;
        this.startTS = i2;
        this.duration = i3;
        this.callType = i4;
        this.simID = i5;
        this.specificNumber = str5;
        this.specificType = str6;
        this.callLogType = str7;
        this.callLogTime = j;
        this.bgColor = num2;
    }


    public CallLogModel copy(Integer num, int i, String str, String str2, String str3, String str4, int i2, int i3, int i4, int i5, String str5, String str6, String str7, long j, Integer num2) {
        return new CallLogModel(num, i, str, str2, str3, str4, i2, i3, i4, i5, str5, str6, str7, j, num2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CallLogModel)) {
            return false;
        }
        CallLogModel callLogModel = (CallLogModel) obj;
        return Intrinsics.areEqual(this.dummyId, callLogModel.dummyId) && this.id == callLogModel.id && Intrinsics.areEqual(this.contactId, callLogModel.contactId) && Intrinsics.areEqual(this.phoneNumber, callLogModel.phoneNumber) && Intrinsics.areEqual(this.name, callLogModel.name) && Intrinsics.areEqual(this.photoUri, callLogModel.photoUri) && this.startTS == callLogModel.startTS && this.duration == callLogModel.duration && this.callType == callLogModel.callType && this.simID == callLogModel.simID && Intrinsics.areEqual(this.specificNumber, callLogModel.specificNumber) && Intrinsics.areEqual(this.specificType, callLogModel.specificType) && Intrinsics.areEqual(this.callLogType, callLogModel.callLogType) && this.callLogTime == callLogModel.callLogTime && Intrinsics.areEqual(this.bgColor, callLogModel.bgColor);
    }

    @Override
    public int getType() {
        return 1;
    }

    public int hashCode() {
        Integer num = this.dummyId;
        int i = 0;
        int hashCode = (((num == null ? 0 : num.hashCode()) * 31) + this.id) * 31;
        String str = this.contactId;
        int hashCode2 = (((((((((((((((((((((((hashCode + (str == null ? 0 : str.hashCode())) * 31) + this.phoneNumber.hashCode()) * 31) + this.name.hashCode()) * 31) + this.photoUri.hashCode()) * 31) + this.startTS) * 31) + this.duration) * 31) + this.callType) * 31) + this.simID) * 31) + this.specificNumber.hashCode()) * 31) + this.specificType.hashCode()) * 31) + this.callLogType.hashCode()) * 31) + CallLog$$ExternalSyntheticBackport0.m(this.callLogTime)) * 31;
        Integer num2 = this.bgColor;
        if (num2 != null) {
            i = num2.hashCode();
        }
        return hashCode2 + i;
    }

    public String toString() {
        return "CallLogModel(dummyId=" + this.dummyId + ", id=" + this.id + ", contactId=" + this.contactId + ", phoneNumber=" + this.phoneNumber + ", name=" + this.name + ", photoUri=" + this.photoUri + ", startTS=" + this.startTS + ", duration=" + this.duration + ", callType=" + this.callType + ", simID=" + this.simID + ", specificNumber=" + this.specificNumber + ", specificType=" + this.specificType + ", callLogType=" + this.callLogType + ", callLogTime=" + this.callLogTime + ", bgColor=" + this.bgColor + ')';
    }

    public Integer getDummyId() {
        return this.dummyId;
    }

    public void setDummyId(Integer num) {
        this.dummyId = num;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getContactId() {
        return this.contactId;
    }

    public void setContactId(String str) {
        this.contactId = str;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String str) {
        this.phoneNumber = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getPhotoUri() {
        return this.photoUri;
    }

    public void setPhotoUri(String str) {
        this.photoUri = str;
    }

    public int getStartTS() {
        return this.startTS;
    }

    public void setStartTS(int i) {
        this.startTS = i;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int i) {
        this.duration = i;
    }

    public int getCallType() {
        return this.callType;
    }

    public void setCallType(int i) {
        this.callType = i;
    }

    public int getSimID() {
        return this.simID;
    }

    public String getSpecificNumber() {
        return this.specificNumber;
    }

    public void setSpecificNumber(String str) {
        this.specificNumber = str;
    }

    public String getSpecificType() {
        return this.specificType;
    }

    public void setSpecificType(String str) {
        this.specificType = str;
    }

    public String getCallLogType() {
        return this.callLogType;
    }

    public long getCallLogTime() {
        return this.callLogTime;
    }

    public Integer getBgColor() {
        return this.bgColor;
    }


    public void setBgColor(Integer bgColor) {
        this.bgColor = bgColor;
    }

    public void setCallLogTime(long callLogTime) {
        this.callLogTime = callLogTime;
    }

    public void setCallLogType(String callLogType) {
        this.callLogType = callLogType;
    }

    public void setSimID(int simID) {
        this.simID = simID;
    }
}
