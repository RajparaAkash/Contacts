package com.contacts.phonecontact.phonebook.dialer.AllModels;

import android.net.Uri;

import java.util.Date;

public class RecentCallLog {

    private int id;
    private Uri photoUri;
    private String callLogTime;
    public Date callLogDate;
    public String dateHeader;
    private String phoneNumber;
    private String callLogType;
    private String contactName;
    private Integer bgColor;
    private String contactId;

    public RecentCallLog() {
    }

    public RecentCallLog(int id, String contactName, String contactId, String callLogType, String callLogTime, Uri photoUri,
                         String phoneNumber, Date callLogDate, String dateHeader, Integer bgColor) {
        this.id = id;
        this.contactName = contactName;
        this.contactId = contactId;
        this.callLogType = callLogType;
        this.callLogTime = callLogTime;
        this.photoUri = photoUri;
        this.phoneNumber = phoneNumber;
        this.callLogDate = callLogDate;
        this.dateHeader = dateHeader;
        this.bgColor = bgColor;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactName() {
        return this.contactName;
    }

    public String getCallLogTime() {
        return this.callLogTime;
    }

    public Uri getPhotoUri() {
        return this.photoUri;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getCallLogType() {
        return this.callLogType;
    }

    public Date getCallLogDate() {
        return this.callLogDate;
    }

    public String getDateHeader() {
        return this.dateHeader;
    }

    public void setDateHeader(String str) {
        this.dateHeader = str;
    }

    public Integer getBgColor() {
        return this.bgColor;
    }

    public void setBgColor(Integer bgColor) {
        this.bgColor = bgColor;
    }

    public String getContactId() {
        return this.contactId;
    }

    public void setContactId(String str) {
        this.contactId = str;
    }
}
