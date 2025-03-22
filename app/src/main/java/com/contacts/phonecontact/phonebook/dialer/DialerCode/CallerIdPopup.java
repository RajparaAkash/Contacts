package com.contacts.phonecontact.phonebook.dialer.DialerCode;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneNumberUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.Utils;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.Contact;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityCallerIdBinding;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.service.SwipeEvents;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class CallerIdPopup {
    private final Context context;
    private final String isSpam;
    private final String name;
    private final String phoneNumber;
    public ActivityCallerIdBinding activityCallerIdBinding;
    private Contact contact;
    private WindowManager.LayoutParams params;
    private WindowManager windowManager;

    public CallerIdPopup(Context context2, String str, String str2, String str3) {
        this.context = context2;
        this.phoneNumber = str;
        this.name = str2;
        this.isSpam = str3;
        activityCallerIdBinding = ActivityCallerIdBinding.inflate(LayoutInflater.from(context2));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -2, Build.VERSION.SDK_INT >= 26 ? 2038 : 2002, 2884264, -2);
        this.params = layoutParams;
        layoutParams.gravity = Gravity.CENTER;
        WindowManager.LayoutParams layoutParams2 = this.params;
        layoutParams2.windowAnimations = 16973827;
        Object systemService = context2.getSystemService(Context.WINDOW_SERVICE);
        windowManager = (WindowManager) systemService;
        initView();
        windowManager.addView(activityCallerIdBinding.getRoot(), this.params);
    }



    public WindowManager.LayoutParams getParams() {
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

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact2) {
        this.contact = contact2;
    }

    private void initView() {
        String str;
        boolean z = true;
        if (!(phoneNumber == null || phoneNumber.length() == 0)) {
            if (!(name == null || name.length() == 0)) {
                activityCallerIdBinding.contactName.setText(this.name);
                activityCallerIdBinding.contactNumber.setText(PhoneNumberUtils.formatNumber(phoneNumber, "IN"));
                activityCallerIdBinding.profileImageLayout.setVisibility(View.VISIBLE);
                activityCallerIdBinding.ivAddContact.setVisibility(View.VISIBLE);
                activityCallerIdBinding.ivProfileSmall.setVisibility(View.VISIBLE);
                activityCallerIdBinding.profileImage.setVisibility(View.GONE);
                activityCallerIdBinding.ivAddContact.setColorFilter(ContextCompat.getColor(context, R.color.app_color));
                activityCallerIdBinding.ivProfileSmall.setColorFilter(ContextCompat.getColor(context, R.color.theme_light_color));
                if (Intrinsics.areEqual(this.isSpam, "1")) {
                    activityCallerIdBinding.topView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red)));
                    activityCallerIdBinding.ivAddContact.setColorFilter(ContextCompat.getColor(context, R.color.red));
                }
            } else {
                contact = Utils.getContact(context, phoneNumber);
                if (contact != null) {
                    String nameSuffix = contact.getNameSuffix();
                    if (nameSuffix == null || nameSuffix.length() == 0) {
                        str = context.getString(R.string.title_private_number);
                    } else {
                        str = contact.getNameSuffix();
                    }
                    activityCallerIdBinding.contactName.setText(str);
                    activityCallerIdBinding.contactNumber.setText(PhoneNumberUtils.formatNumber(phoneNumber, "IN"));
                    activityCallerIdBinding.profileImageLayout.setVisibility(View.GONE);
                    activityCallerIdBinding.ivAddContact.setVisibility(View.GONE);
                    activityCallerIdBinding.profileImage.setVisibility(View.VISIBLE);
                    activityCallerIdBinding.ivProfileSmall.setColorFilter(ContextCompat.getColor(context, R.color.theme_light_color));
                    String contactPhotoUri = contact.getContactPhotoUri();
                    if (!(contactPhotoUri == null || contactPhotoUri.length() == 0)) {
                        Glide.with(context).load(contact.getContactPhotoUri()).into(activityCallerIdBinding.profileImage);
                    } else {
                        String contactPhotoThumbUri = contact.getContactPhotoThumbUri();
                        if (!(contactPhotoThumbUri == null || contactPhotoThumbUri.length() == 0)) {
                            Glide.with(context).load(contact.getContactPhotoThumbUri()).into(activityCallerIdBinding.profileImage);
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
                            activityCallerIdBinding.ivAddContact.setColorFilter(ContextCompat.getColor(context, R.color.app_color));
                            activityCallerIdBinding.profileImageLayout.setVisibility(View.VISIBLE);
                            activityCallerIdBinding.ivAddContact.setVisibility(View.VISIBLE);
                            activityCallerIdBinding.ivProfileSmall.setVisibility(View.VISIBLE);
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
        activityCallerIdBinding.getRoot().setOnTouchListener(new CallerIdPopupInitView2(this));

    }


    public void dismissPopUp(SwipeEvents.SwipeDirection swipeDirection, View view) {
        try {
            int i = WhenMappings.$EnumSwitchMapping$0[swipeDirection.ordinal()];
            if (i == 3 || i == 4) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        close();
                    }
                }, 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setViewEmptyContact() {
        activityCallerIdBinding.contactName.setText(context.getString(R.string.title_private_number));
        activityCallerIdBinding.contactNumber.setText(PhoneNumberUtils.formatNumber(phoneNumber, "IN"));
        activityCallerIdBinding.profileImageLayout.setVisibility(View.VISIBLE);
        activityCallerIdBinding.ivAddContact.setVisibility(View.VISIBLE);
        activityCallerIdBinding.ivProfileSmall.setVisibility(View.VISIBLE);
        activityCallerIdBinding.profileImage.setVisibility(View.GONE);
        activityCallerIdBinding.ivAddContact.setColorFilter(ContextCompat.getColor(context, R.color.app_color));
        activityCallerIdBinding.ivProfileSmall.setColorFilter(ContextCompat.getColor(context, R.color.theme_light_color));
    }

    public void close() {
        try {
            Object systemService = context.getSystemService(Context.WINDOW_SERVICE);
            Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.view.WindowManager");
            ((WindowManager) systemService).removeView(activityCallerIdBinding.getRoot());
            activityCallerIdBinding.getRoot().invalidate();
            ViewParent parent = activityCallerIdBinding.getRoot().getParent();
            Intrinsics.checkNotNull(parent, "null cannot be cast to non-null type android.view.ViewGroup");
            ((ViewGroup) parent).removeAllViews();
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
