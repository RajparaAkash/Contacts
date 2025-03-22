package com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting;

import kotlin.jvm.internal.DefaultConstructorMarker;

public abstract class SortingType {
    public SortingType(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    private SortingType() {
    }

    public static final class Ascending extends SortingType {
        public static final Ascending INSTANCE = new Ascending();

        private Ascending() {
            super(null);
        }
    }

    public static final class Descending extends SortingType {
        public static final Descending INSTANCE = new Descending();

        private Descending() {
            super(null);
        }
    }

}
