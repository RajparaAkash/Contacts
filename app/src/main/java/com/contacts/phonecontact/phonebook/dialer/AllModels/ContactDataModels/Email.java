package com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels;

import androidx.annotation.Keep;

import com.contacts.phonecontact.phonebook.dialer.types.EmailType;

import java.io.Serializable;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Keep
public class Email implements Serializable {
    private Boolean isDefault;
    private String label;
    private EmailType type;
    private String value;

    public Email(String str, EmailType emailType, String str2, Boolean bool) {
        this.value = str;
        this.type = emailType;
        this.label = str2;
        this.isDefault = bool;
    }

    public Email(String str, EmailType emailType, String str2, Boolean bool, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, emailType, str2, (i & 8) != 0 ? false : bool);
    }


    public Email copy(String str, EmailType emailType, String str2, Boolean bool) {
        return new Email(str, emailType, str2, bool);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Email)) {
            return false;
        }
        Email email = (Email) obj;
        return Intrinsics.areEqual(this.value, email.value) && this.type == email.type && Intrinsics.areEqual(this.label, email.label) && Intrinsics.areEqual(this.isDefault, email.isDefault);
    }

    public int hashCode() {
        int hashCode = ((((this.value.hashCode() * 31) + this.type.hashCode()) * 31) + this.label.hashCode()) * 31;
        Boolean bool = this.isDefault;
        return hashCode + (bool == null ? 0 : bool.hashCode());
    }

    public String toString() {
        return "Email(value=" + this.value + ", type=" + this.type + ", label=" + this.label + ", isDefault=" + this.isDefault + ')';
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public EmailType getType() {
        return this.type;
    }

    public void setType(EmailType emailType) {
        this.type = emailType;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String str) {
        this.label = str;
    }

    public Boolean isDefault() {
        return this.isDefault;
    }

    public void setDefault(Boolean bool) {
        this.isDefault = bool;
    }

}
