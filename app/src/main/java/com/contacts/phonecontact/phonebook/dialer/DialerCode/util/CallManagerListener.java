package com.contacts.phonecontact.phonebook.dialer.DialerCode.util;

import android.telecom.Call;

public interface CallManagerListener {
    void onPrimaryCallChanged(Call call);

    void onStateChanged();
}
