package com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ListObject;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.ConstantsKt;

import java.io.Serializable;
import java.util.ArrayList;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Keep
@Entity(tableName = "Contact")
public class Contact extends ListObject implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private int contactId;
    private int contactIdSimple;
    private String namePrefix;
    private String firstName;
    private String firstNameOriginal;
    private String middleName;
    private String surName;
    private String nameSuffix;
    private String contactPhotoUri;
    private String contactPhotoThumbUri;
    private boolean contactIsStared;
    private String ringtone;
    private ArrayList<PhoneNumber> contactNumber;
    private ArrayList<Email> contactEmail;
    private ArrayList<Event> contactEvent;
    private ArrayList<Address> contactAddresses;
    private ArrayList<String> websites;
    private ArrayList<Long> contactGroup;
    private String contactSource;
    private ArrayList<String> contactNotes;
    private String company;
    private String jobPosition;
    private String jobTitle;
    private String mimeType;
    private Integer bgColor;

    public Contact(Integer id, int contactId, int contactIdSimple, String namePrefix, String firstName, String firstNameOriginal, String middleName, String surName, String nameSuffix, String contactPhotoUri, String contactPhotoThumbUri, boolean contactIsStared, String ringtone, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4, ArrayList arrayList5, ArrayList arrayList6, String str10, ArrayList arrayList7, String str11, String str12, String jobTitle, String mimeType, Integer bgColor, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? null : id, contactId, contactIdSimple, namePrefix, firstName, firstNameOriginal, middleName, surName, nameSuffix, (i3 & 512) != 0 ? null : contactPhotoUri, (i3 & 1024) != 0 ? null : contactPhotoThumbUri, contactIsStared, ringtone, arrayList, arrayList2, arrayList3, arrayList4, arrayList5, arrayList6, str10, arrayList7, str11, str12, jobTitle, (16777216 & i3) != 0 ? null : mimeType, (i3 & ConstantsKt.LICENSE_EVENT_BUS) != 0 ? -16776961 : bgColor);
    }

    public Contact(Integer id, int contactId, int contactIdSimple, String namePrefix, String firstName, String firstNameOriginal, String middleName, String surName, String nameSuffix, String contactPhotoUri, String contactPhotoThumbUri, boolean contactIsStared, String ringtone, ArrayList<PhoneNumber> contactNumber, ArrayList<Email> contactEmail, ArrayList<Event> contactEvent, ArrayList<Address> contactAddresses, ArrayList<String> websites, ArrayList<Long> contactGroup, String contactSource, ArrayList<String> contactNotes, String company, String jobPosition, String jobTitle, String mimeType, Integer bgColor) {
        this.id = id;
        this.contactId = contactId;
        this.contactIdSimple = contactIdSimple;
        this.namePrefix = namePrefix;
        this.firstName = firstName;
        this.firstNameOriginal = firstNameOriginal;
        this.middleName = middleName;
        this.surName = surName;
        this.nameSuffix = nameSuffix;
        this.contactPhotoUri = contactPhotoUri;
        this.contactPhotoThumbUri = contactPhotoThumbUri;
        this.contactIsStared = contactIsStared;
        this.ringtone = ringtone;
        this.contactNumber = contactNumber;
        this.contactEmail = contactEmail;
        this.contactEvent = contactEvent;
        this.contactAddresses = contactAddresses;
        this.websites = websites;
        this.contactGroup = contactGroup;
        this.contactSource = contactSource;
        this.contactNotes = contactNotes;
        this.company = company;
        this.jobPosition = jobPosition;
        this.jobTitle = jobTitle;
        this.mimeType = mimeType;
        this.bgColor = bgColor;
    }

    public static Contact copy$default(Contact contact, Integer num, int i, int i2, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, boolean z, String str9, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4, ArrayList arrayList5, ArrayList arrayList6, String str10, ArrayList arrayList7, String str11, String str12, String str13, String str14, Integer num2, int i3, Object obj) {
        return contact.copy((i3 & 1) != 0 ? contact.id : num, (i3 & 2) != 0 ? contact.contactId : i, (i3 & 4) != 0 ? contact.contactIdSimple : i2, (i3 & 8) != 0 ? contact.namePrefix : str, (i3 & 16) != 0 ? contact.firstName : str2, (i3 & 32) != 0 ? contact.firstNameOriginal : str3, (i3 & 64) != 0 ? contact.middleName : str4, (i3 & 128) != 0 ? contact.surName : str5, (i3 & 256) != 0 ? contact.nameSuffix : str6, (i3 & 512) != 0 ? contact.contactPhotoUri : str7, (i3 & 1024) != 0 ? contact.contactPhotoThumbUri : str8, (i3 & 2048) != 0 ? contact.contactIsStared : z, (i3 & 4096) != 0 ? contact.ringtone : str9, (i3 & 8192) != 0 ? contact.contactNumber : arrayList, (i3 & 16384) != 0 ? contact.contactEmail : arrayList2, (i3 & 32768) != 0 ? contact.contactEvent : arrayList3, (i3 & 65536) != 0 ? contact.contactAddresses : arrayList4, (i3 & 131072) != 0 ? contact.websites : arrayList5, (i3 & 262144) != 0 ? contact.contactGroup : arrayList6, (i3 & 524288) != 0 ? contact.contactSource : str10, (i3 & 1048576) != 0 ? contact.contactNotes : arrayList7, (i3 & 2097152) != 0 ? contact.company : str11, (i3 & 4194304) != 0 ? contact.jobPosition : str12, (i3 & 8388608) != 0 ? contact.jobTitle : str13, (i3 & 16777216) != 0 ? contact.mimeType : str14, (i3 & ConstantsKt.LICENSE_EVENT_BUS) != 0 ? contact.bgColor : num2);
    }

    public Contact copy(Integer num, int i, int i2, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, boolean z, String str9, ArrayList<PhoneNumber> arrayList, ArrayList<Email> arrayList2, ArrayList<Event> arrayList3, ArrayList<Address> arrayList4, ArrayList<String> arrayList5, ArrayList<Long> arrayList6, String str10, ArrayList<String> arrayList7, String str11, String str12, String str13, String str14, Integer num2) {
        return new Contact(num, i, i2, str, str2, str3, str4, str5, str6, str7, str8, z, str9, arrayList, arrayList2, arrayList3, arrayList4, arrayList5, arrayList6, str10, arrayList7, str11, str12, str13, str14, num2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Contact)) {
            return false;
        }
        Contact contact = (Contact) obj;
        return Intrinsics.areEqual(this.id, contact.id) && this.contactId == contact.contactId && this.contactIdSimple == contact.contactIdSimple && Intrinsics.areEqual(this.namePrefix, contact.namePrefix) && Intrinsics.areEqual(this.firstName, contact.firstName) && Intrinsics.areEqual(this.firstNameOriginal, contact.firstNameOriginal) && Intrinsics.areEqual(this.middleName, contact.middleName) && Intrinsics.areEqual(this.surName, contact.surName) && Intrinsics.areEqual(this.nameSuffix, contact.nameSuffix) && Intrinsics.areEqual(this.contactPhotoUri, contact.contactPhotoUri) && Intrinsics.areEqual(this.contactPhotoThumbUri, contact.contactPhotoThumbUri) && this.contactIsStared == contact.contactIsStared && Intrinsics.areEqual(this.ringtone, contact.ringtone) && Intrinsics.areEqual(this.contactNumber, contact.contactNumber) && Intrinsics.areEqual(this.contactEmail, contact.contactEmail) && Intrinsics.areEqual(this.contactEvent, contact.contactEvent) && Intrinsics.areEqual(this.contactAddresses, contact.contactAddresses) && Intrinsics.areEqual(this.websites, contact.websites) && Intrinsics.areEqual(this.contactGroup, contact.contactGroup) && Intrinsics.areEqual(this.contactSource, contact.contactSource) && Intrinsics.areEqual(this.contactNotes, contact.contactNotes) && Intrinsics.areEqual(this.company, contact.company) && Intrinsics.areEqual(this.jobPosition, contact.jobPosition) && Intrinsics.areEqual(this.jobTitle, contact.jobTitle) && Intrinsics.areEqual(this.mimeType, contact.mimeType) && Intrinsics.areEqual(this.bgColor, contact.bgColor);
    }

    @Override
    public int getType() {
        return 1;
    }

    public int hashCode() {
        Integer num = this.id;
        int i = 0;
        int hashCode = (((((((((((((((((num == null ? 0 : num.hashCode()) * 31) + this.contactId) * 31) + this.contactIdSimple) * 31) + this.namePrefix.hashCode()) * 31) + this.firstName.hashCode()) * 31) + this.firstNameOriginal.hashCode()) * 31) + this.middleName.hashCode()) * 31) + this.surName.hashCode()) * 31) + this.nameSuffix.hashCode()) * 31;
        String str = this.contactPhotoUri;
        int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
        String str2 = this.contactPhotoThumbUri;
        int hashCode3 = (hashCode2 + (str2 == null ? 0 : str2.hashCode())) * 31;
        boolean z = this.contactIsStared;
        if (z) {
            z = true;
        }
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = z ? 1 : 0;
        int i5 = (hashCode3 + i2) * 31;
        String str3 = this.ringtone;
        int hashCode4 = (((((((((((((((((((((((i5 + (str3 == null ? 0 : str3.hashCode())) * 31) + this.contactNumber.hashCode()) * 31) + this.contactEmail.hashCode()) * 31) + this.contactEvent.hashCode()) * 31) + this.contactAddresses.hashCode()) * 31) + this.websites.hashCode()) * 31) + this.contactGroup.hashCode()) * 31) + this.contactSource.hashCode()) * 31) + this.contactNotes.hashCode()) * 31) + this.company.hashCode()) * 31) + this.jobPosition.hashCode()) * 31) + this.jobTitle.hashCode()) * 31;
        String str4 = this.mimeType;
        int hashCode5 = (hashCode4 + (str4 == null ? 0 : str4.hashCode())) * 31;
        Integer num2 = this.bgColor;
        if (num2 != null) {
            i = num2.hashCode();
        }
        return hashCode5 + i;
    }

    public String toString() {
        return "Contact(id=" + this.id + ", contactId=" + this.contactId + ", contactIdSimple=" + this.contactIdSimple + ", namePrefix=" + this.namePrefix + ", firstName=" + this.firstName + ", firstNameOriginal=" + this.firstNameOriginal + ", middleName=" + this.middleName + ", surName=" + this.surName + ", nameSuffix=" + this.nameSuffix + ", contactPhotoUri=" + this.contactPhotoUri + ", contactPhotoThumbUri=" + this.contactPhotoThumbUri + ", contactIsStared=" + this.contactIsStared + ", ringtone=" + this.ringtone + ", contactNumber=" + this.contactNumber + ", contactEmail=" + this.contactEmail + ", contactEvent=" + this.contactEvent + ", contactAddresses=" + this.contactAddresses + ", websites=" + this.websites + ", contactGroup=" + this.contactGroup + ", contactSource=" + this.contactSource + ", contactNotes=" + this.contactNotes + ", company=" + this.company + ", jobPosition=" + this.jobPosition + ", jobTitle=" + this.jobTitle + ", mimeType=" + this.mimeType + ", bgColor=" + this.bgColor + ')';
    }

    public Integer getId() {
        return this.id;
    }

    public int getContactId() {
        return this.contactId;
    }

    public void setContactId(int i) {
        this.contactId = i;
    }

    public int getContactIdSimple() {
        return this.contactIdSimple;
    }

    public void setContactIdSimple(int i) {
        this.contactIdSimple = i;
    }

    public String getNamePrefix() {
        return this.namePrefix;
    }

    public void setNamePrefix(String str) {
        this.namePrefix = str;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String str) {
        this.firstName = str;
    }

    public String getFirstNameOriginal() {
        return this.firstNameOriginal;
    }

    public void setFirstNameOriginal(String str) {
        this.firstNameOriginal = str;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public void setMiddleName(String str) {
        this.middleName = str;
    }

    public String getSurName() {
        return this.surName;
    }

    public void setSurName(String str) {
        this.surName = str;
    }

    public String getNameSuffix() {
        return this.nameSuffix;
    }

    public void setNameSuffix(String str) {
        this.nameSuffix = str;
    }

    public String getContactPhotoUri() {
        return this.contactPhotoUri;
    }

    public void setContactPhotoUri(String str) {
        this.contactPhotoUri = str;
    }

    public String getContactPhotoThumbUri() {
        return this.contactPhotoThumbUri;
    }

    public void setContactPhotoThumbUri(String str) {
        this.contactPhotoThumbUri = str;
    }

    public boolean getContactIsStared() {
        return this.contactIsStared;
    }

    public void setContactIsStared(boolean z) {
        this.contactIsStared = z;
    }

    public String getRingtone() {
        return this.ringtone;
    }

    public void setRingtone(String str) {
        this.ringtone = str;
    }

    public ArrayList<PhoneNumber> getContactNumber() {
        return this.contactNumber;
    }

    public void setContactNumber(ArrayList<PhoneNumber> arrayList) {
        this.contactNumber = arrayList;
    }

    public ArrayList<Email> getContactEmail() {
        return this.contactEmail;
    }

    public void setContactEmail(ArrayList<Email> arrayList) {
        this.contactEmail = arrayList;
    }

    public ArrayList<Event> getContactEvent() {
        return this.contactEvent;
    }

    public void setContactEvent(ArrayList<Event> arrayList) {
        this.contactEvent = arrayList;
    }

    public ArrayList<Address> getContactAddresses() {
        return this.contactAddresses;
    }

    public void setContactAddresses(ArrayList<Address> arrayList) {
        this.contactAddresses = arrayList;
    }

    public ArrayList<String> getWebsites() {
        return this.websites;
    }

    public void setWebsites(ArrayList<String> arrayList) {
        this.websites = arrayList;
    }

    public ArrayList<Long> getContactGroup() {
        return this.contactGroup;
    }

    public String getContactSource() {
        return this.contactSource;
    }

    public void setContactSource(String str) {
        this.contactSource = str;
    }

    public ArrayList<String> getContactNotes() {
        return this.contactNotes;
    }

    public void setContactNotes(ArrayList<String> arrayList) {
        this.contactNotes = arrayList;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String str) {
        this.company = str;
    }

    public String getJobPosition() {
        return this.jobPosition;
    }

    public void setJobPosition(String str) {
        this.jobPosition = str;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public void setJobTitle(String str) {
        this.jobTitle = str;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String str) {
        this.mimeType = str;
    }

    public Integer getBgColor() {
        return this.bgColor;
    }

    public void setBgColor(Integer num) {
        this.bgColor = num;
    }

    public String getStringToCompare() {
        return copy$default(this, 0, 0, 0, "", "", "", "", "", "", "", null, false, "", new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), "", null, null, null, null, null, null, 66061312, null).toString();
    }

    public String getStringToCompare1() {
        String lowerCase = getNameToDisplay().toLowerCase();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        return copy$default(this, 0, 0, 0, "", lowerCase, null, "", "", "", "", null, false, "", arrayList, arrayList2, arrayList3, arrayList4, new ArrayList(), new ArrayList(), "", arrayList5, null, null, null, null, null, 65012768, null).toString();
    }

    public String getNameToDisplay() {
        String str;
        String obj = StringsKt.trim((CharSequence) (firstName + ' ' + middleName)).toString();
        boolean z = true;
        if (nameSuffix.length() == 0) {
            str = "";
        } else {
            str = ", " + nameSuffix;
        }
        String obj2 = StringsKt.trim((CharSequence) (namePrefix + ' ' + obj + ' ' + surName + str)).toString();
        if (obj2.length() != 0) {
            z = false;
        }
        if (z) {
            return "";
        }
        return obj2;
    }

    public void setContactGroup(ArrayList<Long> contactGroup) {
        this.contactGroup = contactGroup;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
