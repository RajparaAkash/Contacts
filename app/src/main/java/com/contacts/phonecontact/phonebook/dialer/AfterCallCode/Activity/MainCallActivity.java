package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.InputDeviceCompat;

import com.bumptech.glide.Glide;
import com.contacts.phonecontact.phonebook.dialer.Activities.ActivityContactInformation;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Adapters.AdapterCallerScreen;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.AppUtils;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.Constants;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.PermissionHandler;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.PreferencesManager;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.Contact;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Receiver.PhoneStateReceiver1;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Receiver.Utils;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Services.AdLoadingJobService;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Services.CallingServices;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.ConstantsKt;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityMainCallBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public class MainCallActivity extends BaseActivity implements View.OnClickListener {

    public static final Companion Companion = new Companion(null);
    public static MainCallActivity sInstancem;
    private static volatile PreferencesManager sInstance;
    private final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1001;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            recreate();
        }
    };
    public PreferencesManager preferencesManager;
    private ActivityMainCallBinding activitycallMainBinding;
    private String callStatus = "";
    private AdapterCallerScreen adapterCallerScreen;
    private String contactId = "";
    private String contactName = "";
    private boolean firstTime;
    private boolean isCallStatusLogAdded;
    private boolean isLogAdded;
    private int nightModeFlags;
    private boolean night_mode;
    private String number = "";
    private String time = "00:00";
    private CountDownTimer timer;

    @JvmStatic
    public static PreferencesManager getInstance(Context context) {
        return Companion.getInstance(context);
    }


    public PreferencesManager getPreferencesManager() {
        if (preferencesManager != null) {
            return preferencesManager;
        }
        return null;
    }

    public void setPreferencesManager(PreferencesManager preferencesManager2) {
        this.preferencesManager = preferencesManager2;
    }


    public boolean getNight_mode() {
        return this.night_mode;
    }

    public void setNight_mode(boolean z) {
        this.night_mode = z;
    }

    public boolean getFirstTime() {
        return this.firstTime;
    }

    public void setFirstTime(boolean z) {
        this.firstTime = z;
    }

    public int getNightModeFlags() {
        return this.nightModeFlags;
    }

    public void setNightModeFlags(int i) {
        this.nightModeFlags = i;
    }

    public boolean isLogAdded() {
        return this.isLogAdded;
    }

    public void setLogAdded(boolean z) {
        this.isLogAdded = z;
    }

    public boolean isCallStatusLogAdded() {
        return this.isCallStatusLogAdded;
    }

    public void setCallStatusLogAdded(boolean z) {
        this.isCallStatusLogAdded = z;
    }

    @Override
    public void onDestroy() {
        Constants.isActivityShow = false;
        MyApplication.getInstance().isShowingAd = false;
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.e("MainCallActivity-------->", "onCreate");
        MainCallActivity mainCallActivity = this;
        sendToMixpanel("Open_CDO");
        sInstancem = this;
        PreferencesManager.Companion companion = PreferencesManager.Companion;
        Context applicationContext = getApplicationContext();
        setPreferencesManager(companion.getInstance(applicationContext));
        activitycallMainBinding = ActivityMainCallBinding.inflate(getLayoutInflater());
        setContentView(activitycallMainBinding.getRoot());

        /*MyApplication.getInstance().isShowingAd = true;*/

        setThemeData();
        init();
        UIComponent();
        AppUtils.isMyServiceRunning(AdLoadingJobService.class, mainCallActivity);

    }

    private void sendToMixpanel(String str) {
        /*if (PhoneStateReceiver1.isIncomingCallEventSend) {
        }*/
    }

    private void setThemeData() {
        this.nightModeFlags = getApplication().getResources().getConfiguration().uiMode & 48;
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (nightModeFlags == 32 && !night_mode) {
            getWindow().getDecorView().setSystemUiVisibility(0);
        } else if (nightModeFlags == 16 && !night_mode && firstTime) {
            getWindow().getDecorView().setSystemUiVisibility(0);
        }
        Window window = getWindow();
        window.addFlags(Integer.MIN_VALUE);
        window.clearFlags(ConstantsKt.LICENSE_AUDIO_RECORD_VIEW);
    }

    @Override
    public void onStart() {
        super.onStart();
        Constants.isActivityShow = true;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void init() {
        if (getIntent() != null) {
            long startTime = getIntent().getLongExtra(Constants.StartTime, 0);
            long endTime = getIntent().getLongExtra(Constants.EndTime, 0);
            this.time = getTimeDiff(startTime, endTime);
            this.number = String.valueOf(getIntent().getStringExtra(Utils.EXTRA_MOBILE_NUMBER));
            callStatus = String.valueOf(getIntent().getStringExtra(Constants.CallType));
            if (!isCallStatusLogAdded) {
                try {
                    isCallStatusLogAdded = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setAdapterData() {
        adapterCallerScreen = new AdapterCallerScreen(this, this.number);
        adapterCallerScreen.setContactData(contactName, contactId);
        activitycallMainBinding.viewPager.setAdapter(adapterCallerScreen);
        new TabLayoutMediator(activitycallMainBinding.tabLayout, activitycallMainBinding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setIcon(R.drawable.ic_action_call_m);
                    tab.getIcon().setColorFilter(ContextCompat.getColor(MainCallActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
                } else if (position == 1) {
                    tab.setIcon(R.drawable.ic_action_msg_m);
                    tab.getIcon().setColorFilter(ContextCompat.getColor(MainCallActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
                } else if (position == 2) {
                    tab.setIcon(R.drawable.ic_action_block_m);
                    tab.getIcon().setColorFilter(ContextCompat.getColor(MainCallActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
                } else {
                    tab.setIcon(R.drawable.ic_action_notifi_m);
                    tab.getIcon().setColorFilter(ContextCompat.getColor(MainCallActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
                }
            }
        }).attach();
    }

    public void UIComponent() {
        if (!PermissionHandler.getInstance().checkNeverAskAgain(MainCallActivity.this)) {
            callService(MainCallActivity.this);
        }
        activitycallMainBinding.imgCalliCall.setOnClickListener(view -> {
            new Bundle().putString(Constants.CLICK_CALL, Constants.CLICK_CALL);
            com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.Utils.openDialerPad(MainCallActivity.this, number);
            finishAndRemoveTask();
        });


        MyApplication.getInstance().showCallScreenNativeAd(this, findViewById(R.id.flNative));

        if (!AppUtils.isEmptyString(this.number)) {
            if (com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.Utils.getContact(MainCallActivity.this, this.number) == null) {
                activitycallMainBinding.txtAppName.setText(PhoneNumberUtils.formatNumber(this.number, "IN"));
                activitycallMainBinding.ImageView.setVisibility(View.VISIBLE);
                activitycallMainBinding.txtUserProName.setVisibility(View.GONE);
                activitycallMainBinding.callerAvatar.setVisibility(View.GONE);
                activitycallMainBinding.itemTvContactFirstLetter.setVisibility(View.GONE);
            } else {
                Contact contact = com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.Utils.getContact(MainCallActivity.this, this.number);
                this.contactName = contact.getNameSuffix();
                this.contactId = String.valueOf(contact.getContactId());
                activitycallMainBinding.txtAppName.setText(contact.getNameSuffix());
                activitycallMainBinding.ImageView.setVisibility(View.GONE);
                activitycallMainBinding.itemTvContactFirstLetter.setVisibility(View.GONE);
                activitycallMainBinding.callerAvatar.setVisibility(View.GONE);
                String contactPhotoUri = contact.getContactPhotoUri();
                boolean z = true;
                if (!(contactPhotoUri == null || contactPhotoUri.isEmpty())) {
                    activitycallMainBinding.callerAvatar.setVisibility(View.VISIBLE);
                    activitycallMainBinding.txtUserProName.setVisibility(View.GONE);
                    Glide.with(MainCallActivity.this).load(contact.getContactPhotoUri()).into(activitycallMainBinding.callerAvatar);
                } else {
                    String contactPhotoThumbUri = contact.getContactPhotoThumbUri();
                    if (!(contactPhotoThumbUri == null || contactPhotoThumbUri.isEmpty())) {
                        z = false;
                    }
                    if (!z) {
                        activitycallMainBinding.callerAvatar.setVisibility(View.VISIBLE);
                        activitycallMainBinding.txtUserProName.setVisibility(View.GONE);
                        Glide.with(MainCallActivity.this).load(contact.getContactPhotoThumbUri()).into(activitycallMainBinding.callerAvatar);
                    } else {
                        activitycallMainBinding.txtUserProName.setVisibility(View.VISIBLE);
                        activitycallMainBinding.callerAvatar.setVisibility(View.GONE);
                        activitycallMainBinding.txtUserProName.setText(com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.Utils.firstStringer(contact.getNameSuffix()));
                    }
                }
            }
        }
        setAdapterData();
        activitycallMainBinding.txtCalliInfo.setText("" + time);
        activitycallMainBinding.imgAppIcon.setOnClickListener(view -> {
            openContactDetailScreen();
        });
        activitycallMainBinding.profileLayout.setOnClickListener(view -> {
            openContactDetailScreen();
        });
        activitycallMainBinding.txtAppName.setOnClickListener(view -> {
            openContactDetailScreen();
        });
        activitycallMainBinding.durationLayout.setOnClickListener(view -> {
            openContactDetailScreen();
        });
        activitycallMainBinding.txtCallStatus.setText(callStatus);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        Date time2 = Calendar.getInstance().getTime();
        activitycallMainBinding.txtTime.setText(simpleDateFormat.format(time2).toString());
        activitycallMainBinding.txtCalliInfo.setText("" + time);
        checkNotificationTime();
    }

    public void openContactDetailScreen() {
        Intent intent = new Intent(MainCallActivity.this, ActivityContactInformation.class);
        intent.putExtra("selectedContactId", contactId);
        intent.putExtra("selectedContactNumber", number);
        intent.putExtra("openInCDO", true);
        startActivity(intent);
        finish();
    }

    private void checkNotificationTime() {
        Boolean muteNotificationAlways = getPreferencesManager().getMuteNotificationAlways();
        if (!muteNotificationAlways.booleanValue()) {
            Long muteNotificationTime = getPreferencesManager().getMuteNotificationTime();
            if (!muteNotificationTime.equals(0)) {
                long currentTimeMillis = System.currentTimeMillis();
                Long muteNotificationTime2 = getPreferencesManager().getMuteNotificationTime();
                if (currentTimeMillis <= muteNotificationTime2.longValue()) {
                    countDownStart();
                }
            }
        }
    }

    public void callService(Context context) {
        Log.e("TAG 192", "CallingServices: Start");
        Intent intent = new Intent(context, CallingServices.class);
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }


    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (Build.VERSION.SDK_INT >= 23 && i == this.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE && Settings.canDrawOverlays(this)) {
            String[] callPermissionArray = PermissionHandler.getInstance().getCallPermissionArray();
            ActivityCompat.requestPermissions(MainCallActivity.this, callPermissionArray, 1024);
        }
    }

    public BroadcastReceiver getBroadcastReceiver() {
        return this.broadcastReceiver;
    }

    public void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        PhoneStateReceiver1.isIncomingCallEventSend = false;
        finishAndRemoveTask();
        MyApplication.isNeedUpdateRecent = true;
    }

    public void onClick(View view) {
        boolean z = true;
        if (view == null || !Integer.valueOf(view.getId()).equals(Integer.valueOf(R.id.img_main_close))) {
            z = false;
        }
        if (z) {
            new Bundle().putString(Constants.CLICK_CLOSE, Constants.CLICK_CLOSE);
            if (isTaskRoot()) {
                finishAndRemoveTask();
            } else {
                finishAffinity();
            }
        }
    }

    public boolean isDefault(Context context) {
        TelecomManager telecomManager = Build.VERSION.SDK_INT >= 21 ? (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE) : null;
        String str = "";
        if (telecomManager != null && Build.VERSION.SDK_INT >= 23) {
            String defaultDialerPackage = telecomManager.getDefaultDialerPackage();
            str = defaultDialerPackage;
        }
        return Intrinsics.areEqual(str, context.getPackageName());
    }

    @Override
    public void onPause() {
        super.onPause();
        PhoneStateReceiver1.isIncomingCallEventSend = false;
    }

    private void countDownStart() {
        long j;
        long j2;
        long currentTimeMillis = System.currentTimeMillis();
        Long muteNotificationTime = getPreferencesManager().getMuteNotificationTime();
        if (currentTimeMillis > muteNotificationTime.longValue()) {
            Long muteNotificationTime2 = getPreferencesManager().getMuteNotificationTime();
            j2 = muteNotificationTime2.longValue();
            j = System.currentTimeMillis();
        } else {
            Long muteNotificationTime3 = getPreferencesManager().getMuteNotificationTime();
            j2 = muteNotificationTime3.longValue();
            j = System.currentTimeMillis();
        }
        long j3 = j2 - j;
        Log.e("TAG", "countDownStart: " + j3);
        if (j3 > 0) {
            startTimer(j3);
        }
    }

    private void startTimer(long j) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(j, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                getPreferencesManager().setMuteNotification(MainCallActivity.this, -1);
            }
        };
    }

    private String getTimeDiff(long j, long j2) {
        long j3 = j2 - j;
        long j4 = (long) 60;
        long j5 = (j3 / ((long) 1000)) % j4;
        long j6 = (j3 / ((long) 60000)) % j4;
        long j7 = (j3 / ((long) 3600000)) % ((long) 24);
        if (j7 > 0) {
            return AppUtils.addExtraZero(j7) + ':' + AppUtils.addExtraZero(j6) + ':' + AppUtils.addExtraZero(j5);
        }
        return AppUtils.addExtraZero(j6) + ':' + AppUtils.addExtraZero(j5);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        Log.e("------->", String.valueOf(z));
//        fullScreencall(this);
    }

    public void fullScreencall(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT < 23) {
            activity.getWindow().getDecorView().setSystemUiVisibility(InputDeviceCompat.SOURCE_TOUCHSCREEN);
        } else if (Build.VERSION.SDK_INT >= 30) {
            WindowInsetsController windowInsetsController = decorView.getWindowInsetsController();
            if (windowInsetsController != null) {
                windowInsetsController.hide(WindowInsets.Type.navigationBars());
                windowInsetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            int i = activity.getResources().getConfiguration().uiMode & 48;
            if (i != 1 && i != 32) {
                activity.getWindow().getDecorView().setSystemUiVisibility(12290);
            } else if (Build.VERSION.SDK_INT >= 23) {
                decorView.setSystemUiVisibility((decorView.getSystemUiVisibility() & 8192) | 2 | 4096);
            }
        }
    }

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public PreferencesManager getInstance(Context context) {
            if (MainCallActivity.sInstance == null) {
                synchronized (this) {
                    MainCallActivity.sInstance = PreferencesManager.Companion.getInstance(context);
                }
            }
            PreferencesManager preferencesManager = MainCallActivity.sInstance;
            return preferencesManager;
        }
    }
}
