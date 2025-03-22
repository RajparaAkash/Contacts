package com.contacts.phonecontact.phonebook.dialer.DataHelper.database.dao;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;

import java.util.List;

@Keep
@Dao
public interface ContactSourcesDAO {
//    long addAccount(ContactSource contactSource);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAllAccounts(List<ContactSource> list);

//    void deleteAccounts(ContactSource contactSource);

//    void deleteAllAccounts();

    @Query("SELECT * FROM ContactSource WHERE publicName = :str")
    ContactSource getAccountWithName(String str);

    @Query("SELECT * FROM ContactSource")
    List<ContactSource> getAllAccounts();

    @Query("UPDATE ContactSource set isSelected=0")
    void updateAll();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAllAccounts(List<ContactSource> list);

}
