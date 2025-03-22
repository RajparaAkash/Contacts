package com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels;

import androidx.annotation.Keep;

@Keep
public class Language {
    private final int languageResourceId;
    private final String languageCode;
    private final String languageName;
    private boolean isSelected;

    public Language(int i, String str, String str2) {
        this.languageResourceId = i;
        this.languageName = str;
        this.languageCode = str2;
    }

    public String getLanguageName() {
        return this.languageName;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public int getLanguageResourceId() {
        return this.languageResourceId;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

}
