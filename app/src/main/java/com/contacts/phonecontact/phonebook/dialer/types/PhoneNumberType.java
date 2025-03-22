package com.contacts.phonecontact.phonebook.dialer.types;

public enum PhoneNumberType {
    NO_LABEL("No label"),
    MOBILE("Mobile"),
    WORK("Work"),
    HOME("Home"),
    MAIN("Main"),
    WORK_FAX("Work fax"),
    HOME_FOX("Home fax"),
    PAGER("Pager"),
    OTHER("Other"),
    CUSTOM("Custom");

    private final String phoneType;

    private PhoneNumberType(String str) {
        this.phoneType = str;
    }

    public final String getPhoneType() {
        return this.phoneType;
    }
}
