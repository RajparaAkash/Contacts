package com.contacts.phonecontact.phonebook.dialer.DialerCode.util;

public final class NoCall extends PhoneState {
    public static final NoCall INSTANCE = new NoCall();

    private NoCall() {
        super(null);
    }
}
