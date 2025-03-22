package com.contacts.phonecontact.phonebook.dialer.types;

public enum ContactSorting {
    FIRST_NAME(0),
    SURNAME(1);

    private final int selectedType;

    private ContactSorting(int i) {
        this.selectedType = i;
    }

    public final int getSelectedType() {
        return this.selectedType;
    }
}
