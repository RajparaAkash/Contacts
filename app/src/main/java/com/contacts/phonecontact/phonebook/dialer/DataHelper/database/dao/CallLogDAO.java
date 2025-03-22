package com.contacts.phonecontact.phonebook.dialer.DataHelper.database.dao;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogModel;

import java.util.List;

@Keep
@Dao
public interface CallLogDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAllHistory(List<CallLogModel> list);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long addCallLog(CallLogModel callLogModel);

    @Query("DELETE FROM CallLogModel")
    void deleteAllCallLog();

//    @Query("DELETE FROM `CallLogModel` WHERE `dummyId` = callLogModel.dummyId")
    @Delete
    void deleteCallLog(CallLogModel callLogModel);

    @Query("SELECT * FROM CallLogModel")
    List<CallLogModel> getCallLog();

    void refreshAccounts(CallLogModel callLogModel);

    public static final class DefaultImpls {
        public static void refreshAccounts(CallLogDAO callLogDAO, CallLogModel callLogModel) {
            callLogDAO.deleteCallLog(callLogModel);
            callLogDAO.addCallLog(callLogModel);
        }
    }

}
