package com.contacts.phonecontact.phonebook.dialer.DataHelper.repo;

import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogModel;

import java.util.List;

import kotlin.Unit;
import kotlin.coroutines.Continuation;

public interface HistoryRepo {
    Object addCallLog(CallLogModel callLogModel, Continuation<? super Long> continuation);

    Object deleteAllCallLog(Continuation<? super Unit> continuation);

    Object deleteCallLog(CallLogModel callLogModel, Continuation<? super Unit> continuation);

    List<CallLogModel> getCallLog();

    Object refreshAccounts(CallLogModel callLogModel, Continuation<? super Unit> continuation);
}
