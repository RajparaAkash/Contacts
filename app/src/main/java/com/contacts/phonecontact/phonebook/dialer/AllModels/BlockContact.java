package com.contacts.phonecontact.phonebook.dialer.AllModels;

import androidx.annotation.Keep;

import com.contacts.phonecontact.phonebook.dialer.types.PhoneNumberType;

import java.io.Serializable;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Keep
public class BlockContact implements Serializable {
    private String contactName;
    private Boolean isDefault;
    private String label;
    private String normalizedNumber;
    private PhoneNumberType type;
    private String value;

    public BlockContact(String str, String str2, PhoneNumberType phoneNumberType, String str3, String str4, Boolean bool) {
        this.value = str;
        this.contactName = str2;
        this.type = phoneNumberType;
        this.label = str3;
        this.normalizedNumber = str4;
        this.isDefault = bool;
    }

    public BlockContact(String str, String str2, PhoneNumberType phoneNumberType, String str3, String str4, Boolean bool, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, phoneNumberType, str3, str4, (i & 32) != 0 ? false : bool);
    }



    public BlockContact copy(String str, String str2, PhoneNumberType phoneNumberType, String str3, String str4, Boolean bool) {
        return new BlockContact(str, str2, phoneNumberType, str3, str4, bool);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BlockContact)) {
            return false;
        }
        BlockContact blockContact = (BlockContact) obj;
        return Intrinsics.areEqual(this.value, blockContact.value) && Intrinsics.areEqual(this.contactName, blockContact.contactName) && this.type == blockContact.type && Intrinsics.areEqual(this.label, blockContact.label) && Intrinsics.areEqual(this.normalizedNumber, blockContact.normalizedNumber) && Intrinsics.areEqual(this.isDefault, blockContact.isDefault);
    }

    public int hashCode() {
        int hashCode = ((((((((this.value.hashCode() * 31) + this.contactName.hashCode()) * 31) + this.type.hashCode()) * 31) + this.label.hashCode()) * 31) + this.normalizedNumber.hashCode()) * 31;
        Boolean bool = this.isDefault;
        return hashCode + (bool == null ? 0 : bool.hashCode());
    }

    public String toString() {
        return "BlockContact(value=" + this.value + ", contactName=" + this.contactName + ", type=" + this.type + ", label=" + this.label + ", normalizedNumber=" + this.normalizedNumber + ", isDefault=" + this.isDefault + ')';
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String str) {
        this.contactName = str;
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
