package com.contacts.phonecontact.phonebook.dialer.Viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ModelQuickResponse;
import com.contacts.phonecontact.phonebook.dialer.database.QuickResponseDatabase;

import java.util.List;

public class QuickResponseViewModel extends AndroidViewModel {
    public QuickResponseViewModel(Application application) {
        super(application);
    }

    public LiveData<List<ModelQuickResponse>> getResponseList() {
        return QuickResponseDatabase.getInstance(getApplication().getApplicationContext()).quickResponseDao().getAllResponse();
    }

    public void insertHistory(ModelQuickResponse webHistory) {
        QuickResponseDatabase.getInstance(getApplication().getApplicationContext()).quickResponseDao().insert(webHistory);
    }

    public void deleteHistory(ModelQuickResponse webHistory) {
        QuickResponseDatabase.getInstance(getApplication().getApplicationContext()).quickResponseDao().delete(webHistory.getId());
    }

    public void deleteAllHistory() {
        QuickResponseDatabase.getInstance(getApplication().getApplicationContext()).quickResponseDao().deleteAll();
    }

}
