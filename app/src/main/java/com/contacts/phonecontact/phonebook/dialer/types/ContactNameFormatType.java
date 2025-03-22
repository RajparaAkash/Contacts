package com.contacts.phonecontact.phonebook.dialer.types;

public enum ContactNameFormatType {
    FIRST_NAME_FIRST(0),
    SURNAME_FIRST(1);

    private final int selectedType;

    private ContactNameFormatType(int i) {
        this.selectedType = i;
    }

    public final int getSelectedType() {
        return this.selectedType;
    }
}
