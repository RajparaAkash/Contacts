package com.contacts.phonecontact.phonebook.dialer.types;

public enum ThemeType {
    LIGHT(0),
    DARK(1),
    SYSTEM_DEFAULT(2);

    private final int selectedType;

    private ThemeType(int i) {
        this.selectedType = i;
    }

    public final int getSelectedType() {
        return this.selectedType;
    }
}
