package com.contacts.phonecontact.phonebook.dialer.database.dao;

import androidx.annotation.Keep;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ModelQuickResponse;

import java.util.List;


@Keep
@Dao
public interface QuickResponseDao {

    @Delete
    void delete(ModelQuickResponse quickResponse);

    @Query("DELETE FROM QuickResponse")
    void deleteAll();

    @Query("DELETE FROM QuickResponse WHERE id = :id")
    void delete(int id);

    @Query("SELECT * FROM QuickResponse WHERE id = :id")
    ModelQuickResponse findById(int id);

    @Query("SELECT * FROM QuickResponse")
    LiveData<List<ModelQuickResponse>> getAllResponse();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ModelQuickResponse quickResponse);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ModelQuickResponse quickResponse);

}
