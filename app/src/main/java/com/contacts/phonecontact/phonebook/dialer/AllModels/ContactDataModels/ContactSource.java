package com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Keep
@Entity(tableName = "ContactSource")
public class ContactSource {
    @NonNull
    @PrimaryKey
    private String publicName;
    private String name;
    private String type;
    private Integer color;
    private boolean isSelected;

    public ContactSource() {

    }

    public ContactSource(String publicName, String name, String type, Integer color, boolean isSelected) {
        this.publicName = publicName;
        this.name = name;
        this.type = type;
        this.color = color;
        this.isSelected = isSelected;
    }

    public ContactSource(String str, String str2, String str3, Integer num, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, str3, (i & 8) != 0 ? -16776961 : num, (i & 16) != 0 ? false : z);
    }


    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ContactSource)) {
            return false;
        }
        ContactSource contactSource = (ContactSource) obj;
        return Intrinsics.areEqual(this.publicName, contactSource.publicName) && Intrinsics.areEqual(this.name, contactSource.name) && Intrinsics.areEqual(this.type, contactSource.type) && Intrinsics.areEqual(this.color, contactSource.color) && this.isSelected == contactSource.isSelected;
    }

    public int hashCode() {
        int hashCode = ((((this.publicName.hashCode() * 31) + this.name.hashCode()) * 31) + this.type.hashCode()) * 31;
        Integer num = this.color;
        int hashCode2 = (hashCode + (num == null ? 0 : num.hashCode())) * 31;
        boolean z = this.isSelected;
        if (z) {
            z = true;
        }
        int i = z ? 1 : 0;
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        return hashCode2 + i;
    }

    public String toString() {
        return "ContactSource(publicName=" + this.publicName + ", name=" + this.name + ", type=" + this.type + ", color=" + this.color + ", isSelected=" + this.isSelected + ')';
    }

    public String getPublicName() {
        return this.publicName;
    }

    public void setPublicName(String str) {
        this.publicName = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public Integer getColor() {
        return this.color;
    }

    public void setColor(Integer num) {
        this.color = num;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

}
