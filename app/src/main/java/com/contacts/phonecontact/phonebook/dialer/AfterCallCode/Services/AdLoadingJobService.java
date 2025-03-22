package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AdLoadingJobService extends Service {
//    private AdvertiseHandler advertiseHandler;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return 2;
    }

    public void onCreate() {
        super.onCreate();
        Log.e("------>", "AdLoadingJobService");
//        advertiseHandler = AdvertiseHandler.getInstance();
//        advertiseHandler.loadNativeAdsWithoutActivity(null, getString(R.string.getNative_id), this);
    }

}
