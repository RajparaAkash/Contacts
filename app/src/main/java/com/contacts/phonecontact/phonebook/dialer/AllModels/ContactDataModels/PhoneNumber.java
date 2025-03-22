package com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels;

import androidx.annotation.Keep;

import com.contacts.phonecontact.phonebook.dialer.types.PhoneNumberType;

import java.io.Serializable;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Keep
public class PhoneNumber implements Serializable {
    private Boolean isDefault;
    private String label;
    private String normalizedNumber;
    private PhoneNumberType type;
    private String value;

    public PhoneNumber(String str, PhoneNumberType phoneNumberType, String str2, String str3, Boolean bool) {
        this.value = str;
        this.type = phoneNumberType;
        this.label = str2;
        this.normalizedNumber = str3;
        this.isDefault = bool;
    }

    public PhoneNumber(String str, PhoneNumberType phoneNumberType, String str2, String str3, Boolean bool, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, phoneNumberType, str2, str3, (i & 16) != 0 ? false : bool);
    }


    public PhoneNumber copy(String str, PhoneNumberType phoneNumberType, String str2, String str3, Boolean bool) {
        return new PhoneNumber(str, phoneNumberType, str2, str3, bool);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PhoneNumber)) {
            return false;
        }
        PhoneNumber phoneNumber = (PhoneNumber) obj;
        return Intrinsics.areEqual(this.value, phoneNumber.value) && this.type == phoneNumber.type && Intrinsics.areEqual(this.label, phoneNumber.label) && Intrinsics.areEqual(this.normalizedNumber, phoneNumber.normalizedNumber) && Intrinsics.areEqual(this.isDefault, phoneNumber.isDefault);
    }

    public int hashCode() {
        int hashCode = ((((((this.value.hashCode() * 31) + this.type.hashCode()) * 31) + this.label.hashCode()) * 31) + this.normalizedNumber.hashCode()) * 31;
        Boolean bool = this.isDefault;
        return hashCode + (bool == null ? 0 : bool.hashCode());
    }

    public String toString() {
        return "PhoneNumber(value=" + this.value + ", type=" + this.type + ", label=" + this.label + ", normalizedNumber=" + this.normalizedNumber + ", isDefault=" + this.isDefault + ')';
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public PhoneNumberType getType() {
        return this.type;
    }

    public void setType(PhoneNumberType phoneNumberType) {
        this.type = phoneNumberType;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String str) {
        this.label = str;
    }

    public String getNormalizedNumber() {
        return this.normalizedNumber;
    }

    public void setNormalizedNumber(String str) {
        this.normalizedNumber = str;
    }

    public Boolean isDefault() {
        return this.isDefault;
    }

    public void setDefault(Boolean bool) {
        this.isDefault = bool;
    }

}
