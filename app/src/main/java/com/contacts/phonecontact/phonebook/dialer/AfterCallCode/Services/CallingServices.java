package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.provider.CallLog;

import androidx.core.app.NotificationCompat;
import androidx.core.view.InputDeviceCompat;

import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Activity.MainCallActivity;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.Constants;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;

import java.util.Date;

public class CallingServices extends Service {
    public static final String ACTION_START = "action_start";
    public static final String EXTRA_ACTION = "extra_action";
    private int lastShownNotificationId;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return Service.START_STICKY;
    }

    public void onCreate() {
        super.onCreate();
        createNotification();
    }

    public void createNotification() {
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                MyApplication.getInstance().isShowingAd = true;
                Intent intent = new Intent(this, MainCallActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("flag_noty", 1);
                BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= 26 && notificationManager != null) {
                    notificationManager.createNotificationChannel(createChannel("channel_id"));
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id");
                MyApplication.getInstance().isShowingAd = true;
                Intent intent2 = new Intent(this, MainCallActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent2.putExtra("flag_noty", 1);
                PendingIntent.getActivity(this, 10, intent2, 201326592);
                Notification build = builder.setPriority(1).setContentTitle("").setContentText("Protecting your privacy").setSmallIcon(R.drawable.app_logo).setOngoing(true).setShowWhen(false).setSilent(true).setAutoCancel(false).setDefaults(1).build();
                builder.build().flags |= 32;
                if (Build.VERSION.SDK_INT >= 31) {
                    builder.setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE);
                }
                if (notificationManager == null) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= 29) {
                    try {
                        startForeground(2, build, ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else {
                    startForeground(2, build);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NotificationChannel createChannel(String str) {
        if (Build.VERSION.SDK_INT < 26) {
            return null;
        }
        NotificationChannel notificationChannel = new NotificationChannel(str, "notification", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription("this private chanel");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(InputDeviceCompat.SOURCE_ANY);
        notificationChannel.setShowBadge(false);
        notificationChannel.setSound(null, null);
        return notificationChannel;
    }

    public void onDestroy() {
        super.onDestroy();
    }


    private String getCallDetails() {
        StringBuffer stringBuffer = new StringBuffer();
        Cursor query = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int columnIndex = query.getColumnIndex("number");
        int columnIndex2 = query.getColumnIndex("type");
        int columnIndex3 = query.getColumnIndex("date");
        int columnIndex4 = query.getColumnIndex("duration");
        stringBuffer.append("Call Details :");
        while (query.moveToNext()) {
            String string = query.getString(columnIndex);
            String string2 = query.getString(columnIndex2);
            Date date = new Date(Long.valueOf(query.getString(columnIndex3)).longValue());
            String string3 = query.getString(columnIndex4);
            String str = null;
            int parseInt = Integer.parseInt(string2);
            if (parseInt == 1) {
                str = Constants.INCOMING;
            } else if (parseInt == 2) {
                str = Constants.OUTGOING;
            } else if (parseInt == 3) {
                str = "MISSED";
            }
            stringBuffer.append("\nPhone Number:--- " + string + " \nCall Type:--- " + str + " \nCall Date:--- " + date + " \nCall duration in sec :--- " + string3);
            stringBuffer.append("\n----------------------------------");
        }
        query.close();
        return stringBuffer.toString();
    }

}
