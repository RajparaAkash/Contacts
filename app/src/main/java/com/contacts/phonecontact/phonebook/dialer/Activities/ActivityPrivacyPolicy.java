package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CompoundButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.contacts.phonecontact.phonebook.dialer.BuildConfig;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.PreferencesManager;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityPrivacyPolicyBinding;

public class ActivityPrivacyPolicy extends BaseActivity {

    ActivityPrivacyPolicyBinding binding;
    ActivityResultLauncher<Intent> launcherMakeDefaultApp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MyApplication.getInstance().showNativeBannerAd(this, findViewById(R.id.flNativeBanner));

        launcherMakeDefaultApp = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            if (MyApplication.getInstance().getResponseData() != null && BuildConfig.VERSION_CODE == MyApplication.getInstance().getResponseData().getAppVersion() && MyApplication.getInstance().getResponseData().getIsDefault() == 1) {
                if (ContaxtExtUtils.isDefault(ActivityPrivacyPolicy.this)) {
                    if (!isAllPermissionGranted(ActivityPrivacyPolicy.this)) {
                        Intent intent = new Intent(ActivityPrivacyPolicy.this, ActivityPermission.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }  else {
                        Intent intent = new Intent(ActivityPrivacyPolicy.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            } else {
                if (!isAllPermissionGranted(ActivityPrivacyPolicy.this)) {
                    Intent intent = new Intent(ActivityPrivacyPolicy.this, ActivityPermission.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }  else {
                    Intent intent = new Intent(ActivityPrivacyPolicy.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        SpannableString spannableString = new SpannableString("By clicking get Started, you are agree to\nPrivacy Policy and Terms of Services");

        ForegroundColorSpan foregroundColorSpanRed = new ForegroundColorSpan(getColor(R.color.app_color));
        ForegroundColorSpan foregroundColorSpanGreen = new ForegroundColorSpan(getColor(R.color.app_color));

        spannableString.setSpan(foregroundColorSpanRed, 41, 56, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(foregroundColorSpanGreen, 61, 78, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                MyApplication.getInstance().openPrivacy(ActivityPrivacyPolicy.this);
            }
        };
        spannableString.setSpan(clickableSpan, 41, 56, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                MyApplication.getInstance().openTerms(ActivityPrivacyPolicy.this);
            }
        };
        spannableString.setSpan(clickableSpan2, 61, 78, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        binding.tvPrivacy.setText(spannableString);
        binding.tvPrivacy.setMovementMethod(LinkMovementMethod.getInstance());

        binding.cbPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    binding.tvGetStarted.setText(getString(R.string.accept_and_continue));
                    binding.tvGetStarted.setEnabled(true);
                    binding.tvGetStarted.setBackgroundResource(R.drawable.app_btn_round);
                } else {
                    binding.tvGetStarted.setText(getString(R.string.privacy_get_started));
                    binding.tvGetStarted.setEnabled(false);
                    binding.tvGetStarted.setBackgroundResource(R.drawable.app_btn_round_disable);
                }
            }
        });

        binding.tvGetStarted.setOnClickListener(view -> {
            if (binding.cbPrivacy.isChecked()) {
                MyApplication.getInstance().setShowPrivacyScreen(false);

                Intent intent = new Intent(this, ActivityLanguage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    private boolean isAllPermissionGranted(Context context) {
        return PreferencesManager.hasPermission(context,
                new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_PHONE_STATE});
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
