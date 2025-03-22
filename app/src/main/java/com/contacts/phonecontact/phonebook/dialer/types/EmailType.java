package com.contacts.phonecontact.phonebook.dialer.types;

public enum EmailType {
    MOBILE("Mobile"),
    WORK("Work"),
    HOME("Home"),
    MAIN("Main"),
    OTHER("Other");

    private final String emailType;

    private EmailType(String str) {
        this.emailType = str;
    }

    public final String getEmailType() {
        return this.emailType;
    }
}
