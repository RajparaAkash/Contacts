package com.contacts.phonecontact.phonebook.dialer.DataHelper.repo_impl;

import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogModel;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.dao.CallLogDAO;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.repo.HistoryRepo;

import java.util.List;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.Boxing;

public class HistoryRepoIMPL implements HistoryRepo {
    private final CallLogDAO mContactDatabase;

    public HistoryRepoIMPL(CallLogDAO callLogDAO) {
        this.mContactDatabase = callLogDAO;
    }

    @Override
    public List<CallLogModel> getCallLog() {
        return this.mContactDatabase.getCallLog();
    }

    @Override
    public Object addCallLog(CallLogModel callLogModel, Continuation<? super Long> continuation) {
        return Boxing.boxLong(mContactDatabase.addCallLog(callLogModel));
    }

    @Override
    public Object deleteAllCallLog(Continuation<? super Unit> continuation) {
        mContactDatabase.deleteAllCallLog();
        return Unit.INSTANCE;
    }

    @Override
    public Object deleteCallLog(CallLogModel callLogModel, Continuation<? super Unit> continuation) {
        mContactDatabase.deleteCallLog(callLogModel);
        return Unit.INSTANCE;
    }

    @Override
    public Object refreshAccounts(CallLogModel callLogModel, Continuation<? super Unit> continuation) {
        mContactDatabase.refreshAccounts(callLogModel);
        return Unit.INSTANCE;
    }

}
