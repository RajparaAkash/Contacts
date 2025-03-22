package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogModel;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.repo.HistoryRepo;

import java.util.List;

public class LoadAllCallLogHelper {
    private final HistoryRepo callLog;

    public LoadAllCallLogHelper(HistoryRepo historyRepo) {
        this.callLog = historyRepo;
    }

    public List<CallLogModel> invoke() {
        return this.callLog.getCallLog();
    }

}
