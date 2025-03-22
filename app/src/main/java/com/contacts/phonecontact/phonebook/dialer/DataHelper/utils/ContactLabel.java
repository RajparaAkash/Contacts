package com.contacts.phonecontact.phonebook.dialer.DataHelper.utils;

public enum ContactLabel {
    EMPTY("No label"),
    MOBILE("Mobile"),
    WORK("Work"),
    HOME("Home"),
    MAIN("Main"),
    WORK_FAX("Work fax"),
    HOME_FAX("Home fax"),
    PAGER("Pager"),
    OTHER("Other"),
    CUSTOM("Custom");

    private final String contactType;

    private ContactLabel(String str) {
        this.contactType = str;
    }

    public final String getContactType() {
        return this.contactType;
    }
}
