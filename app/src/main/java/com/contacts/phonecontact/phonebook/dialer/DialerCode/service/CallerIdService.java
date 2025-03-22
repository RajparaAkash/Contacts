package com.contacts.phonecontact.phonebook.dialer.DialerCode.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.PhoneNumberUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.Utils;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.Contact;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.ConstantsUtils;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityCallerIdBinding;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class CallerIdService extends Service {
    public ActivityCallerIdBinding activityCallerIdBinding;
    private Contact contact;
    private String isSpam = "";
    private String name = "";
    private WindowManager.LayoutParams params;
    private String phoneNumber = "";
    private WindowManager windowManager;


    public IBinder onBind(Intent intent) {
        return null;
    }

    public WindowManager.LayoutParams getLayoutParams() {
        return this.params;
    }

    public void setParams(WindowManager.LayoutParams layoutParams) {
        this.params = layoutParams;
    }

    public WindowManager getWindowManager() {
        return this.windowManager;
    }

    public void setWindowManager(WindowManager windowManager2) {
        this.windowManager = windowManager2;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String str) {
        this.phoneNumber = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String isSpam() {
        return this.isSpam;
    }

    public void setSpam(String str) {
        this.isSpam = str;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact2) {
        this.contact = contact2;
    }

    public void onCreate() {
        super.onCreate();
        activityCallerIdBinding = ActivityCallerIdBinding.inflate(LayoutInflater.from(this));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -2, Build.VERSION.SDK_INT >= 26 ? 2038 : 2002, 2884264, -2);
        this.params = layoutParams;
        Intrinsics.checkNotNull(layoutParams);
        layoutParams.gravity = Gravity.CENTER;
        WindowManager.LayoutParams layoutParams2 = this.params;
        Intrinsics.checkNotNull(layoutParams2);
        layoutParams2.windowAnimations = 16973827;
        Object systemService = getSystemService(Context.WINDOW_SERVICE);
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.view.WindowManager");
        WindowManager windowManager2 = (WindowManager) systemService;
        this.windowManager = windowManager2;
        Intrinsics.checkNotNull(windowManager2);
        windowManager2.addView(activityCallerIdBinding.getRoot(), this.params);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if ((intent != null ? intent.getStringExtra(ConstantsUtils.PHONE_NUMBER) : null) != null) {
            String stringExtra = intent.getStringExtra(ConstantsUtils.PHONE_NUMBER);
            Intrinsics.checkNotNull(stringExtra);
            this.phoneNumber = StringsKt.replace(StringsKt.replace(stringExtra, "tel:", "", false), "%2B", "+", false);
            String stringExtra2 = intent.getStringExtra(ConstantsUtils.CALLER_NAME);
            Intrinsics.checkNotNull(stringExtra2);
            this.name = stringExtra2;
            String stringExtra3 = intent.getStringExtra(ConstantsUtils.IS_SPAM);
            Intrinsics.checkNotNull(stringExtra3);
            this.isSpam = stringExtra3;
        }
        initView();
        return Service.START_STICKY;
    }

    private void initView() {
        String str;
        String str2 = phoneNumber;
        boolean z = true;
        if (!(str2 == null || str2.length() == 0)) {
            String str3 = this.name;
            if (!(str3 == null || str3.length() == 0)) {
                activityCallerIdBinding.contactName.setText(this.name);
                activityCallerIdBinding.contactNumber.setText(PhoneNumberUtils.formatNumber(phoneNumber, "IN"));
                activityCallerIdBinding.profileImageLayout.setVisibility(View.VISIBLE);
                activityCallerIdBinding.ivAddContact.setVisibility(View.VISIBLE);
                activityCallerIdBinding.profileImage.setVisibility(View.GONE);
                CallerIdService callerIdService = this;
                activityCallerIdBinding.ivAddContact.setColorFilter(ContextCompat.getColor(callerIdService, R.color.app_color));
                activityCallerIdBinding.ivProfileSmall.setColorFilter(ContextCompat.getColor(callerIdService, R.color.theme_light_color));
                if (Intrinsics.areEqual(isSpam, "1")) {
                    activityCallerIdBinding.topView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(callerIdService, R.color.red)));
                    activityCallerIdBinding.ivAddContact.setColorFilter(ContextCompat.getColor(callerIdService, R.color.red));
                }
            } else {
                CallerIdService callerIdService2 = this;
                contact = Utils.getContact(callerIdService2, phoneNumber);
                if (contact != null) {
                    String nameSuffix = contact.getNameSuffix();
                    if (nameSuffix == null || nameSuffix.length() == 0) {
                        str = getString(R.string.title_private_number);
                    } else {
                        str = contact.getNameSuffix();
                    }
                    activityCallerIdBinding.contactName.setText(str);
                    activityCallerIdBinding.contactNumber.setText(PhoneNumberUtils.formatNumber(phoneNumber, "IN"));
                    activityCallerIdBinding.profileImageLayout.setVisibility(View.GONE);
                    activityCallerIdBinding.ivAddContact.setVisibility(View.GONE);
                    activityCallerIdBinding.profileImage.setVisibility(View.VISIBLE);
                    activityCallerIdBinding.ivProfileSmall.setColorFilter(ContextCompat.getColor(callerIdService2, R.color.theme_light_color));
                    String contactPhotoUri = contact.getContactPhotoUri();
                    if (!(contactPhotoUri == null || contactPhotoUri.length() == 0)) {
                        Glide.with(callerIdService2).load(contact.getContactPhotoUri()).into(activityCallerIdBinding.profileImage);
                    } else {
                        String contactPhotoThumbUri = contact.getContactPhotoThumbUri();
                        if (!(contactPhotoThumbUri == null || contactPhotoThumbUri.length() == 0)) {
                            Glide.with(callerIdService2).load(contact.getContactPhotoThumbUri()).into(activityCallerIdBinding.profileImage);
                        } else {
                            if (contact != null) {
                                String nameSuffix2 = contact.getNameSuffix();
                                if (!(nameSuffix2 == null || nameSuffix2.length() == 0)) {
                                    activityCallerIdBinding.profileImageLayout.setVisibility(View.VISIBLE);
                                    activityCallerIdBinding.ivAddContact.setVisibility(View.GONE);
                                    activityCallerIdBinding.profileImage.setVisibility(View.GONE);
                                    String obj = StringsKt.trim((CharSequence) contact.getNameSuffix()).toString();
                                    if (obj.length() <= 0) {
                                        z = false;
                                    }
                                    if (z) {
                                        activityCallerIdBinding.tvFirstLetter.setText(String.valueOf(obj.charAt(0)));
                                    }
                                }
                            }
                            activityCallerIdBinding.ivAddContact.setColorFilter(ContextCompat.getColor(callerIdService2, R.color.app_color));
                            activityCallerIdBinding.profileImageLayout.setVisibility(View.VISIBLE);
                            activityCallerIdBinding.ivAddContact.setVisibility(View.VISIBLE);
                            activityCallerIdBinding.profileImage.setVisibility(View.GONE);
                        }
                    }
                } else {
                    setViewEmptyContact();
                }
            }
        } else {
            setViewEmptyContact();
        }
        activityCallerIdBinding.actionClose.setOnClickListener(view -> {
            if (activityCallerIdBinding.getRoot() != null) {
                windowManager.removeView(activityCallerIdBinding.getRoot());
            }
        });
        activityCallerIdBinding.getRoot().setOnTouchListener(new CallerIdServiceInitView2(this));
    }

    public void dismissPopUp(SwipeEvents.SwipeDirection swipeDirection, View view) {
        try {
            int i = WhenMappings.$EnumSwitchMapping$0[swipeDirection.ordinal()];
            if (i == 3 || i == 4) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (activityCallerIdBinding.getRoot() != null) {
                            windowManager.removeView(activityCallerIdBinding.getRoot());
                        }
                    }
                }, 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setViewEmptyContact() {
        activityCallerIdBinding.contactName.setText(getString(R.string.title_private_number));
        activityCallerIdBinding.contactNumber.setText(PhoneNumberUtils.formatNumber(phoneNumber, "IN"));
        activityCallerIdBinding.profileImageLayout.setVisibility(View.VISIBLE);
        activityCallerIdBinding.ivAddContact.setVisibility(View.VISIBLE);
        activityCallerIdBinding.profileImage.setVisibility(View.GONE);
        CallerIdService callerIdService = this;
        activityCallerIdBinding.ivAddContact.setColorFilter(ContextCompat.getColor(callerIdService, R.color.app_color));
        activityCallerIdBinding.ivProfileSmall.setColorFilter(ContextCompat.getColor(callerIdService, R.color.theme_light_color));
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            if (activityCallerIdBinding != null && activityCallerIdBinding.getRoot() != null) {
                windowManager.removeView(activityCallerIdBinding.getRoot());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class WhenMappings {
        public static final int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[SwipeEvents.SwipeDirection.values().length];
            iArr[SwipeEvents.SwipeDirection.TOP.ordinal()] = 1;
            iArr[SwipeEvents.SwipeDirection.BOTTOM.ordinal()] = 2;
            iArr[SwipeEvents.SwipeDirection.RIGHT.ordinal()] = 3;
            iArr[SwipeEvents.SwipeDirection.LEFT.ordinal()] = 4;
            $EnumSwitchMapping$0 = iArr;
        }
    }

}
