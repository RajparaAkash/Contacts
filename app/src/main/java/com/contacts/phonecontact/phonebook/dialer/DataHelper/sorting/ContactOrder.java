package com.contacts.phonecontact.phonebook.dialer.DataHelper.sorting;

import kotlin.jvm.internal.DefaultConstructorMarker;

public abstract class ContactOrder {
    private final SortingType sortingType;

    public ContactOrder(SortingType sortingType2, DefaultConstructorMarker defaultConstructorMarker) {
        this(sortingType2);
    }

    private ContactOrder(SortingType sortingType2) {
        this.sortingType = sortingType2;
    }

    public final SortingType getSortingType() {
        return this.sortingType;
    }

    public static final class Title extends ContactOrder {
        public Title(SortingType sortingType) {
            super(sortingType, null);
        }
    }

    public static final class Date extends ContactOrder {
        public Date(SortingType sortingType) {
            super(sortingType, null);
        }
    }

}
