package com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels;

import androidx.annotation.Keep;

import java.io.Serializable;

import kotlin.jvm.internal.Intrinsics;

@Keep
public class Organization implements Serializable {
    private String company;
    private String jobPosition;
    private String jobTitle;

    public Organization(String str, String str2, String str3) {
        this.company = str;
        this.jobPosition = str2;
        this.jobTitle = str3;
    }


    public Organization copy(String str, String str2, String str3) {
        return new Organization(str, str2, str3);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Organization)) {
            return false;
        }
        Organization organization = (Organization) obj;
        return Intrinsics.areEqual(this.company, organization.company) && Intrinsics.areEqual(this.jobPosition, organization.jobPosition) && Intrinsics.areEqual(this.jobTitle, organization.jobTitle);
    }

    public int hashCode() {
        return (((this.company.hashCode() * 31) + this.jobPosition.hashCode()) * 31) + this.jobTitle.hashCode();
    }

    public String toString() {
        return "Organization(company=" + this.company + ", jobPosition=" + this.jobPosition + ", jobTitle=" + this.jobTitle + ')';
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

}
