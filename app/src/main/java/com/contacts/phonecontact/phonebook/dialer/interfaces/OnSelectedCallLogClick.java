package com.contacts.phonecontact.phonebook.dialer.interfaces;

import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogModel;

public interface OnSelectedCallLogClick {
    void onClick(CallLogModel callLogModel);

    void onDelete(CallLogModel callLogModel);
}
