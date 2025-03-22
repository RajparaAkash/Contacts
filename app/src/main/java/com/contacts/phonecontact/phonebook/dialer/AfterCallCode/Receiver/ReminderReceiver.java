package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.InputDeviceCompat;

import com.contacts.phonecontact.phonebook.dialer.R;

public class ReminderReceiver extends BroadcastReceiver {
    String channelId = "CallStateService";
    Context mContext;
    int reminderId;
    String title;

    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        try {
            this.title = intent.getStringExtra(Utils.EXTRA_REMINDER_NAME);
            this.reminderId = intent.getIntExtra(Utils.EXTRA_REMINDER_ID, 0);
            showNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNotification() {
        NotificationCompat.Builder builder;
        if (TextUtils.isEmpty(title)) {
            title = mContext.getString(R.string.no_title);
        }
        Uri defaultUri = RingtoneManager.getDefaultUri(2);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(createChannel(channelId));
            }
            builder = new NotificationCompat.Builder(mContext, channelId);
        } else {
            builder = new NotificationCompat.Builder(mContext, channelId);
        }
        builder.setPriority(1).setContentTitle(title).setDefaults(1).setSmallIcon(R.drawable.png_logo).setStyle(new NotificationCompat.DecoratedCustomViewStyle()).setColor(ContextCompat.getColor(this.mContext, R.color.app_color)).setVibrate(new long[]{100, 200, 400, 600, 800, 1000}).setSound(defaultUri).setOngoing(false).setAutoCancel(true);
        notificationManager.notify(reminderId, builder.build());
    }

    public NotificationChannel createChannel(String str) {
        Uri defaultUri = RingtoneManager.getDefaultUri(2);
        AudioAttributes build = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(str, "notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("this private chanel");
            notificationChannel.enableLights(true);
            notificationChannel.setSound(defaultUri, build);
            notificationChannel.setLightColor(InputDeviceCompat.SOURCE_ANY);
        }
        return notificationChannel;
    }

}
