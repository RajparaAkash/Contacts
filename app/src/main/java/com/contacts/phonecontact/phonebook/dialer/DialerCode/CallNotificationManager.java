package com.contacts.phonecontact.phonebook.dialer.DialerCode;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.contacts.phonecontact.phonebook.dialer.Activities.CallActivity;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Receiver.NotifyFlashOnOff;
import com.contacts.phonecontact.phonebook.dialer.AllModels.CallContact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.UserContact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.BlockContactHelper;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.CallManager;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.RemoteViewsKt;
import com.contacts.phonecontact.phonebook.dialer.Utils.CallContactAvatarHelper;
import com.contacts.phonecontact.phonebook.dialer.Utils.CallUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.ConstantsUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Receiver.CallActionReceiver;

import java.util.concurrent.atomic.AtomicInteger;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class CallNotificationManager {

    private final int DECLINE_CALL_CODE = 1;
    private final AtomicInteger atomicInteger;
    private final CallContactAvatarHelper callContactAvatarHelper;
    private final Context context;
    private final NotificationManager notificationManager;
    NotifyFlashOnOff notifyFlashOn;
    private int ACCEPT_CALL_CODE;
    private int CALL_NOTIFICATION_ID = 1;
    private String CHANNEL_ID;

    public CallNotificationManager(Context context2) {
        this.context = context2;
        this.notificationManager = ContaxtExtUtils.getNotificationManager(context2);
        this.callContactAvatarHelper = new CallContactAvatarHelper(context2);
        this.CHANNEL_ID = "call_channel";
        this.atomicInteger = new AtomicInteger(0);
    }

    public String getCHANNEL_ID() {
        return this.CHANNEL_ID;
    }

    public void setCHANNEL_ID(String str) {
        this.CHANNEL_ID = str;
    }

    Ringtone ringtone;

    public void setupNotification() {
        String string = null;
        Label_0052:
        {
            try {
                final Call primaryCall = CallManager.Companion.getPrimaryCall();
                if (primaryCall != null) {
                    final Call.Details details = primaryCall.getDetails();
                    if (details != null) {
                        final Uri handle = details.getHandle();
                        if (handle != null) {
                            string = handle.toString();
                            break Label_0052;
                        }
                    }
                }
            } catch (final NullPointerException ex) {
            }
            string = null;
        }
        if (string != null) {
            final String decode = Uri.decode(string);
            if (StringsKt.startsWith(decode, "tel:", false)) {
                final String substringAfter$default = StringsKt.substringAfter(decode, "tel:", "");
                final UserContact contact = getContact(context, substringAfter$default);
                CallContact callContact;
                if (contact == null) {
                    callContact = new CallContact("", "", substringAfter$default, "");
                } else {
                    String s = null;
                    Label_0185:
                    {
                        if (contact.getContactPhotoUri() != null) {
                            s = contact.getContactPhotoUri();
                        } else {
                            if (contact.getContactPhotoThumbUri() == null) {
                                s = "";
                                break Label_0185;
                            }
                            s = contact.getContactPhotoThumbUri();
                        }
                        Intrinsics.checkNotNull((Object) s);
                    }
                    callContact = new CallContact(contact.getNameSuffix(), s, substringAfter$default, "");
                    callContact.setRingtoneUri(contact.getRingtoneUri());

//                    try {
////                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//                        if (contact.getRingtoneUri() != null && !contact.getRingtoneUri().isEmpty()) {
//                            ringtone = RingtoneManager.getRingtone(context, Uri.parse(contact.getRingtoneUri()));
//                            ringtone.play();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.e("fatal3", "setupNotification exe: " + e.getMessage());
//                    }
                    Log.e("fatal3", "setupNotification: " + contact.getRingtoneUri() );
                }
                final Bitmap callContactAvatar = callContactAvatarHelper.getCallContactAvatar(callContact);
                final Integer state = CallManager.Companion.getState();
                int priority = 0;
//                Label_0261:
//                {
                if (ContaxtExtUtils.getPowerManager(context).isInteractive()) {
                    if (state != null) {
                        if (state == 2) {
                            priority = 1;
//                                break Label_0261;
                        }
                    }
                }
//                    priority = 0;
//                }
                String channel_ID;
                if (priority != 0) {
                    channel_ID = "simple_dialer_call_high_priority";
                } else {
                    channel_ID = "simple_dialer_call";
                }
                this.CHANNEL_ID = channel_ID;
                if (CallUtils.isOreoPlus()) {
                    int n;
                    if (priority != 0) {
                        n = 4;
                    } else {
                        n = 3;
                    }
                    String s2;
                    if (priority != 0) {
                        s2 = "call_notification_channel_high_priority";
                    } else {
                        s2 = "call_notification_channel";
                    }
                    final NotificationChannel notificationChannel = new NotificationChannel(channel_ID, (CharSequence) s2, n);
                    notificationChannel.setSound((Uri) null, null);
                    this.notificationManager.createNotificationChannel(notificationChannel);
                }
                final PendingIntent activity = PendingIntent.getActivity(context, 0, CallActivity.Companion.getStartIntent(context), PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_MUTABLE);
                final Intent intent = new Intent(context, (Class) CallActionReceiver.class);
                intent.setAction(ConstantsUtils.ACCEPT_CALL);
                final PendingIntent broadcast = PendingIntent.getBroadcast(context, ACCEPT_CALL_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_MUTABLE);
                final Intent intent2 = new Intent(context, (Class) CallActionReceiver.class);
                intent2.setAction(ConstantsUtils.DECLINE_CALL);
                final PendingIntent broadcast2 = PendingIntent.getBroadcast(context, DECLINE_CALL_CODE, intent2, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_MUTABLE);
                String s3;
                if (!callContact.getName().isEmpty()) {
                    s3 = callContact.getName();
                } else {
                    if (MyApplication.getInstance().getBlockUnknownNum()) {
                        CallManager.Companion.reject();
                        new BlockContactHelper(context, StringsKt.replace(callContact.getNumber(), "-", "", false)).invoke();
                    }
                    s3 = callContact.getNumber();
                }
                int n2 = 0;
                if (state != null) {
                    if (state == Call.STATE_RINGING) {
                        n2 = R.string.incoming_call;
                        if (MyApplication.getInstance().getIsLedFlash()) {
                            startThread();
                        }
                    } else if (state == Call.STATE_DIALING) {
                        n2 = R.string.dialing;
                    } else if (state == Call.STATE_DISCONNECTED) {
                        n2 = R.string.call_ended;
                    } else if (state == Call.STATE_DISCONNECTING) {
                        n2 = R.string.call_ending;
                    } else {
                        n2 = R.string.ongoing_call;
//                        if (ringtone != null && ringtone.isPlaying()) {
//                            ringtone.stop();
//                        }
                        if (notifyFlashOn != null && notifyFlashOn.isRunning()) {
                            notifyFlashOn.stop();
                        }
                    }
                } else {
                    n2 = R.string.ongoing_call;
//                    if (ringtone != null && ringtone.isPlaying()) {
//                        ringtone.stop();
//                    }
                    if (notifyFlashOn != null && notifyFlashOn.isRunning()) {
                        notifyFlashOn.stop();
                    }
                }

                final RemoteViews customContentView = new RemoteViews(context.getPackageName(), R.layout.call_notification);
                RemoteViewsKt.setText(customContentView, R.id.notification_caller_name, s3);
                final String string2 = context.getString(n2);
                RemoteViewsKt.setText(customContentView, R.id.notification_call_status, string2);
                RemoteViewsKt.setVisibleIf(customContentView, R.id.notification_accept_call, state == Call.STATE_RINGING);
                customContentView.setOnClickPendingIntent(R.id.notification_decline_call, broadcast2);
                customContentView.setOnClickPendingIntent(R.id.notification_accept_call, broadcast);
                if (callContactAvatar != null) {
                    RemoteViewsKt.setVisibleIf(customContentView, R.id.notification_thumbnail, true);
                    RemoteViewsKt.setVisibleIf(customContentView, R.id.profileImage, false);
                    RemoteViewsKt.setVisibleIf(customContentView, R.id.itemTvContactFirstLetter, false);
                    customContentView.setImageViewBitmap(R.id.notification_thumbnail, this.callContactAvatarHelper.getCircularBitmap(callContactAvatar));
                } else if (callContact.getName().length() > 0) {
                    RemoteViewsKt.setVisibleIf(customContentView, R.id.notification_thumbnail, false);
                    RemoteViewsKt.setVisibleIf(customContentView, R.id.profileImage, false);
                    RemoteViewsKt.setVisibleIf(customContentView, R.id.itemTvContactFirstLetter, true);
                    RemoteViewsKt.setText(customContentView, R.id.itemTvContactFirstLetter, String.valueOf(callContact.getName().charAt(0)));
                } else {
                    RemoteViewsKt.setVisibleIf(customContentView, R.id.itemTvContactFirstLetter, false);
                    RemoteViewsKt.setVisibleIf(customContentView, R.id.notification_thumbnail, true);
                    RemoteViewsKt.setVisibleIf(customContentView, R.id.profileImage, true);
                }
                final NotificationCompat.Builder setStyle = new NotificationCompat.Builder(context, channel_ID)
                        .setSmallIcon(R.drawable.app_logo).setPriority(priority).setCategory("call")
                        .setCustomContentView(customContentView).setContentIntent(activity)
                        .setSound((Uri) null)
                        .setStyle((NotificationCompat.Style) new NotificationCompat.DecoratedCustomViewStyle());
                if (priority != 0) {
                    setStyle.setFullScreenIntent(activity, true);
                }
                final Notification build = setStyle.build();
                this.notificationManager.notify(this.getID(), build);
            }
        }
    }


    public int getID() {
//        CALL_NOTIFICATION_ID = atomicInteger.incrementAndGet();
        CALL_NOTIFICATION_ID = 1500;
        return CALL_NOTIFICATION_ID;
    }

    public void cancelNotification() {
        this.notificationManager.cancel(CALL_NOTIFICATION_ID);
//        if (ringtone != null && ringtone.isPlaying()) {
//            ringtone.stop();
//        }
        if (notifyFlashOn != null && notifyFlashOn.isRunning()) {
            notifyFlashOn.stop();
        }
    }

    public UserContact getContact(Context context2, String str) {
        UserContact userContact = null;
        try {
            Cursor query = context2.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), new String[]{"display_name", "photo_uri", "photo_thumb_uri", "contact_id", "custom_ringtone"}, null, null, null);
            if (query == null) {
                return null;
            }
            if (query.moveToFirst()) {
                String string = query.getString(query.getColumnIndexOrThrow("display_name"));
                String string2 = query.getString(query.getColumnIndexOrThrow("photo_uri"));
                String string3 = query.getString(query.getColumnIndexOrThrow("photo_thumb_uri"));
                int i = query.getInt(query.getColumnIndexOrThrow("contact_id"));
                userContact = new UserContact(i, string, string2, string3);
                userContact.setRingtoneUri(query.getString(query.getColumnIndexOrThrow("custom_ringtone")));
            }
            if (!query.isClosed()) {
                query.close();
            }
            return userContact;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userContact;
    }


    private void startThread() {
        if (notifyFlashOn == null) {
            notifyFlashOn = new NotifyFlashOnOff(context, 500, 500, 0);
//            if (mBattery > getBattery()) {
            new Thread(notifyFlashOn).start();
//            }
        } else if (notifyFlashOn != null && !notifyFlashOn.isRunning()/* && mBattery > getBattery()*/) {
            notifyFlashOn.start();
        }
    }

}
