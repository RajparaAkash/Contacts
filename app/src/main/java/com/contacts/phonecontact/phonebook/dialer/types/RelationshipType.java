package com.contacts.phonecontact.phonebook.dialer.types;

public enum RelationshipType {
    NO_LABEL("No label"),
    ASSISTANT("Assistant"),
    BROTHER("Brother"),
    CHILD("Child"),
    PARTNER("Partner"),
    FATHER("Father"),
    MOTHER("Mother"),
    FRIEND("Friend"),
    SISTER("Sister");

    private final String relationType;

    private RelationshipType(String str) {
        this.relationType = str;
    }

    public final String getRelationType() {
        return this.relationType;
    }
}
