package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterLanguage;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.LocaleHelper;
import com.contacts.phonecontact.phonebook.dialer.Utils.PreferencesManager;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityLanguageBinding;

public class ActivityLanguage extends BaseActivity {

    ActivityLanguageBinding binding;
    AdapterLanguage adapterLanguage;
    private boolean back;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(getColor(R.color.lang_toolbar));

        MyApplication.getInstance().showNativeBannerAd(this, findViewById(R.id.flNativeBanner));

        back = getIntent().getBooleanExtra("BACK", false);
        if (back) {
            binding.tvTitle.setPadding(0, 0, 0, 0);
            binding.imgBack.setVisibility(View.VISIBLE);

        } else {
            binding.tvTitle.setPadding(50, 0, 0, 0);
            binding.imgBack.setVisibility(View.GONE);
        }
        binding.imgBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.listLanguages.setLayoutManager(new LinearLayoutManager(ActivityLanguage.this));
        adapterLanguage = new AdapterLanguage(ActivityLanguage.this);
        binding.listLanguages.setAdapter(adapterLanguage);
        findViewById(R.id.imgRight).setOnClickListener(view -> {
            MyApplication.getInstance().setLanguage(adapterLanguage.getSelectedLang());
            MyApplication.getInstance().setShowLanguageScreen(false);
            LocaleHelper.setLocale(ActivityLanguage.this, MyApplication.getInstance().getLanguage());

            if (back) {
                navigateTONext();
            } else {
                MyApplication.getInstance().showInnerInterstitialAd(this, () -> {
                    navigateTONext();
                });
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (back) {
            MyApplication.getInstance().showInnerInterstitialAd(this, () -> {
                finish();
            });
        } else {
            finish();
        }
    }


    private boolean isAllPermissionGranted(Context context) {
        return PreferencesManager.hasPermission(context, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_PHONE_STATE});
    }

    private void navigateTONext() {
        if (back) {
            openNextScreen();
        } else {
//            if (MyApplication.getInstance().getShowPrivacyScreen()) {
//                Intent intent = new Intent(ActivityLanguage.this, ActivityPrivacyPolicy.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            } else if (ContaxtExtUtils.isQPlus()) {
//                if (ContaxtExtUtils.isDefault(ActivityLanguage.this)) {
//                    Intent intent = new Intent(ActivityLanguage.this, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(ActivityLanguage.this, ActivitySetAsDefaultBlock.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//            }/* else if (ContaxtExtUtils.isQPlus()) {
//                if (ContaxtExtUtils.isDefaultCallerId(ActivityLanguage.this)) {
//                    if (isAllPermissionGranted(ActivityLanguage.this)) {
//                        Intent intent = new Intent(ActivityLanguage.this, MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(ActivityLanguage.this, ActivityPermission.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
//                } else {
//                    Intent intent = new Intent(ActivityLanguage.this, ActivitySetAsDefault.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//            }*/ else if (isAllPermissionGranted(ActivityLanguage.this)) {
//                Intent intent = new Intent(ActivityLanguage.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            } else {
//                Intent intent = new Intent(ActivityLanguage.this, ActivityPermission.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }


//            if (MyApplication.getInstance().getResponseData() != null && BuildConfig.VERSION_CODE == MyApplication.getInstance().getResponseData().getAppVersion() && MyApplication.getInstance().getResponseData().getIsDefault() == 1) {
            if (ContaxtExtUtils.isQPlus()) {
                if (ContaxtExtUtils.isDefault(ActivityLanguage.this)) {
                    Intent intent = new Intent(ActivityLanguage.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ActivityLanguage.this, ActivitySetAsDefaultBlock.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } else if (isAllPermissionGranted(ActivityLanguage.this)) {
                Intent intent = new Intent(ActivityLanguage.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ActivityLanguage.this, ActivityPermission.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
//            } else {
//                if (isAllPermissionGranted(ActivityLanguage.this)) {
//                    Intent intent = new Intent(ActivityLanguage.this, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(ActivityLanguage.this, ActivityPermission.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//            }

            finish();
        }
    }

    private void openNextScreen() {
        Intent intent = new Intent(ActivityLanguage.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
