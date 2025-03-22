package com.contacts.phonecontact.phonebook.dialer.AllModels;

import androidx.annotation.Keep;

import com.contacts.phonecontact.phonebook.dialer.Utils.BasicUtils;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;

import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Keep
public class SimpleContact implements Comparable<SimpleContact> {
    private static int sorting = -1;
    private final int contactId;
    private final int rawId;
    private ArrayList<String> anniversaries;
    private ArrayList<String> birthdays;
    private String name;
    private ArrayList<PhoneNumber> phoneNumbers;
    private String photoUri;

    public SimpleContact(int i, int i2, String str, String str2, ArrayList<PhoneNumber> arrayList, ArrayList<String> arrayList2, ArrayList<String> arrayList3) {
        this.rawId = i;
        this.contactId = i2;
        this.name = str;
        this.photoUri = str2;
        this.phoneNumbers = arrayList;
        this.birthdays = arrayList2;
        this.anniversaries = arrayList3;
    }


    public int getRawId() {
        return this.rawId;
    }

    public int getContactId() {
        return this.contactId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getPhotoUri() {
        return this.photoUri;
    }

    public void setPhotoUri(String str) {
        this.photoUri = str;
    }

    public ArrayList<PhoneNumber> getPhoneNumbers() {
        return this.phoneNumbers;
    }

    public void setPhoneNumbers(ArrayList<PhoneNumber> arrayList) {
        this.phoneNumbers = arrayList;
    }

    public ArrayList<String> getBirthdays() {
        return this.birthdays;
    }

    public void setBirthdays(ArrayList<String> arrayList) {
        this.birthdays = arrayList;
    }

    public ArrayList<String> getAnniversaries() {
        return this.anniversaries;
    }

    public void setAnniversaries(ArrayList<String> arrayList) {
        this.anniversaries = arrayList;
    }

    public int compareTo(SimpleContact simpleContact) {
        int i;
        int i2 = sorting;
        if (i2 == -1) {
            return compareByFullName(simpleContact);
        }
        if ((i2 & 65536) != 0) {
            i = compareByFullName(simpleContact);
        } else {
            i = Intrinsics.compare(this.rawId, simpleContact.rawId);
        }
        return (sorting & 1024) != 0 ? i * -1 : i;
    }

    private int compareByFullName(final SimpleContact simpleContact) {
        final String normalizeString = BasicUtils.normalizeString(this.name);
        final String normalizeString2 = BasicUtils.normalizeString(simpleContact.name);
        final CharSequence charSequence = normalizeString;
        final Character firstOrNull = StringsKt.firstOrNull(charSequence);
        final int n = 0;
        final int n2 = 1;
        Label_0105:
        {
            if (firstOrNull == null || !Character.isLetter(firstOrNull)) {
                break Label_0105;
            }
            final Character firstOrNull2 = StringsKt.firstOrNull((CharSequence) normalizeString2);
            if (firstOrNull2 == null || Character.isLetter(firstOrNull2)) {
                break Label_0105;
            }
            return -1;
        }
        final Character firstOrNull3 = StringsKt.firstOrNull(charSequence);
        if (firstOrNull3 != null && !Character.isLetter(firstOrNull3)) {
            final Character firstOrNull4 = StringsKt.firstOrNull((CharSequence) normalizeString2);
            if (firstOrNull4 != null && Character.isLetter(firstOrNull4)) {
                return n2;
            }
        }
        int compareTo;
        if (charSequence.length() == 0 && normalizeString2.length() > 0) {
            compareTo = n2;
        } else {
            if (charSequence.length() > 0) {
                int n3 = n;
                if (normalizeString2.length() == 0) {
                    n3 = 1;
                }
                if (n3 != 0) {
                    return -1;
                }
            }
            compareTo = StringsKt.compareTo(normalizeString, normalizeString2, true);
        }
        return compareTo;
    }


}
